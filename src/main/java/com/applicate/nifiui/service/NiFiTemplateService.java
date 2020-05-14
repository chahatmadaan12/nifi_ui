package com.applicate.nifiui.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.StyleConstants.CharacterConstants;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.applicate.nifiui.config.constants.ConnectionConstants;
import com.applicate.nifiui.config.constants.NiFiApiURLConstants;
import com.applicate.nifiui.configuration.provider.GlobalConfigurationProvider;
import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.applicate.nifiui.dbmanager.dao.beans.TemplateDetails;
import com.applicate.nifiui.dbmanager.dao.dboperation.ConnectionDAO;
import com.applicate.nifiui.dbmanager.dao.dboperation.TemplateDetailsDAO;
import com.applicate.nifiui.helper.TemplateHelper;
import com.applicate.nifiui.helper.TemplateHelper.TemplateKeys;
import com.applicate.utils.FileUtils;
import com.applicate.utils.RestClient;
import com.applicate.utils.StringUtils;
import com.applicate.utils.XmlHepler;

@Service
public class NiFiTemplateService {

	private Logger log = LoggerFactory.getLogger(NiFiTemplateService.class);
	
	private static final String templatePath = FileUtils.getAbsolutePath("/configs/applicate/templates/"),REGEX = "@@[^@]*@@",REPLACE="@@";;

	@Autowired
	private TemplateDetailsDAO templateDetailsDAO;
	
	@Autowired
	private TemplateHelper templateHelper;
	
	@Autowired
	private ConnectionDAO connectionDAO;
	
	@Autowired
	private GlobalConfigurationProvider provider;
	
	@Autowired
	private RestClient client;
	
	@Autowired
	private TemplateDetailsService templateDetailsService;
	
	public String createAndUpload(String id) {
		TemplateDetails templateDetails = templateDetailsDAO.findById(id).get();
		Connection extractor = connectionDAO.findById(templateDetails.getExtractorId()).get();
		Connection loader = connectionDAO.findById(templateDetails.getLoaderId()).get();
		String response = null, processGroupId = provider.get(templateDetails.getLob()).getStringConstants("Processor_Group_Id"),
        serverUrl = provider.get(templateDetails.getLob()).getStringConstants("serverURL");
		deleteIfTemplateExist(templateDetails,serverUrl);
		try {
			JSONObject templateData = templateHelper.getReplacebleValueAsJSON(templateDetails,extractor,loader);
			TemplateKeys templateKey = getTemplateName(extractor.getType(),loader.getType());
			String templateString = prepareTemplate(templateHelper.getTemplate(templateKey.toString()),templateData);
			response = uploadTemplate(templateString,processGroupId,serverUrl);
			String templateId = templateHelper.getTemplateIdFromResponse(response);
			intanstiateTemplate(processGroupId,templateId,serverUrl,templateDetails.getLob(),id,false);
			uploadServices(templateKey,templateData,processGroupId,serverUrl,templateDetails.getLob(),id);
		} catch (Exception e) { 
			log.error(e.getMessage(),e);
			return e.getMessage();
		}
		return response;
	}

	private void deleteIfTemplateExist(TemplateDetails templateDetails, String serverUrl) {
	  try {
		if(!StringUtils.isEmpty(templateDetails.getTemplateId()))
		   client.delete(serverUrl+FileUtils.getPathWithReplaceValue(NiFiApiURLConstants.TEMPLATE_DELETE, "id", templateDetails.getTemplateId()));
	  }catch(Exception e) {
		  log.error(e.getMessage(),e);
	  }
	}

	private void intanstiateTemplate(String processGroupId, String templateId, String serverUrl, String lob, String templateDetailsId,Boolean isService) {
		Double originX = isService ? 0.0 : getOriginX(lob);
		JSONObject json = new JSONObject().put("originX", originX)
				.put("originY", 0.0)
				.put("templateId", templateId)
				.put("disconnectedNodeAcknowledged", true);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE.toString());
		client.setHeaders(headers);
		client.post(serverUrl+FileUtils.getPathWithReplaceValue(NiFiApiURLConstants.TEMPLATE_INSTANTIATES, "id", processGroupId),json.toString());
		if(!isService)
		  templateDetailsService.updateTemplateDetails(new JSONObject().put("originX", originX).put("active",true).put("templateId", templateId), templateDetailsId);
	}

	private Double getOriginX(String lob) {
		Double maxOriginY = templateDetailsDAO.getMaxOriginX(lob);
		return maxOriginY+1500.00;
	}

	private void uploadServices(TemplateKeys templateKey,JSONObject templateData, String processGroupId, String serverUrl, String lob, String templateDetailsId) {
		for(String service : templateKey.getServices()) {
			setServiceTemplateName(templateData,service);
			String templateString = prepareTemplate("services/"+service,templateData);
			try {
				String templateId = templateHelper.getTemplateIdFromResponse(uploadTemplate(templateString,processGroupId,serverUrl));
				intanstiateTemplate(processGroupId, templateId, serverUrl, lob, templateDetailsId,true);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	private void setServiceTemplateName(JSONObject templateData, String service) {
       String serviceTemplateName = service.split("\\.")[0];
       if(ConnectionConstants.connectionServices.contains(serviceTemplateName)) {
    	   if(serviceTemplateName.contains("Extractor")) {
    		  serviceTemplateName+="("+templateData.optString("Extractor_param6")+")";
    		  templateData.put("Template_Name", serviceTemplateName);
    	   }else if(serviceTemplateName.contains("Loader")) {
    		  serviceTemplateName+="("+templateData.optString("Loader_param6")+")";
     		  templateData.put("Template_Name", serviceTemplateName);
    	   }
       }
	}

	private TemplateKeys getTemplateName(String type1, String type2) {
		if(type1!=null&&type2!=null)
		  return TemplateKeys.valueOf((type1+"_TO_"+type2).toUpperCase());
		return null;
	}

	private String prepareTemplate(String templateName,JSONObject templateData) {
		String templateString=null;
	    Pattern compile = Pattern.compile(REGEX);
        try {
			templateString = new String(Files.readAllBytes(Paths.get(templatePath+templateName)), StandardCharsets.UTF_8);
			Matcher matcher = compile.matcher(templateString);
			while(matcher.find()) {
				String argument = matcher.group();
				templateString = templateString.replace(argument, XmlHepler.replaceSpecialCharacter(templateData.optString(argument.replaceAll(REPLACE, ""))));
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
        System.out.println(templateString);
        return templateString;
	}
	
    private String uploadTemplate(String templateString, String processGroupId, String url) throws IOException {
		File file = createTempTemplateFile(templateString);
		String apiUrl = url + FileUtils.getPathWithReplaceValue(NiFiApiURLConstants.TEMPLATE_UPLOAD, "id", processGroupId);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("template", new FileSystemResource(file));
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("Content-Type", MediaType.MULTIPART_FORM_DATA.toString());
		client.setHeaders(hashMap);
        String response = client.postWithFile(apiUrl, body);
        file.deleteOnExit();
        return response;
	}

	private File createTempTemplateFile(String templateString) throws IOException {
		File file = File.createTempFile("tempTemplate", ".xml");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(templateString);
        bw.close();
		return file;
	}
	
	public static void main(String[] args) {
    String replaceAll = "{\"HEADER-VALIDATION\":{\"ALLOW-EXTRA-HEADERS\":true,\"HEADERS\":[\"size\",\"subCategory\"]},\"CONTENT-VALIDATION\":{\"size\":{\"nullable\":\"true\",\"unique\":\"false\",\"regex\":{\"message\":\"size should not be\",\"regex\":\"^[a-zA-Z0-9,]*$\"}},\"subCategory\":{\"nullable\":\"false\",\"unique\":\"false\",\"regex\":{\"message\":\"abc\",\"regex\":\"^[a-zA-Z0-9,]*$\"}}}}".replace("$", "$");
	System.out.println(replaceAll);
	}
	
}

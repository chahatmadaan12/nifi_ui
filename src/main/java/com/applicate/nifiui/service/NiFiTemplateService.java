package com.applicate.nifiui.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.http.entity.ContentType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

@Service
public class NiFiTemplateService {
	
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
		try {
			JSONObject templateData = templateHelper.getReplacebleValueAsJSON(templateDetails,extractor,loader);
			TemplateKeys templateKey = getTemplateName(extractor.getType(),loader.getType());
			String templateString = prepareTemplate(templateHelper.getTemplate(templateKey.toString()),templateData);
			response = uploadTemplate(templateString,processGroupId,serverUrl);
			String templateId = templateHelper.getTemplateIdFromResponse(response);
			intanstiateTemplate(processGroupId,templateId,serverUrl,templateDetails.getLob(),id,false);
			uploadServices(templateKey,templateData,processGroupId,serverUrl,templateDetails.getLob(),id);
		} catch (Exception e) { 
			e.printStackTrace();
			return e.getMessage();
		}
		return response;
	}

	private void intanstiateTemplate(String processGroupId, String templateId, String serverUrl, String lob, String templateDetailsId,Boolean isService) {
		Double originY = isService ? 0.0 : getOriginY(lob);
		JSONObject json = new JSONObject().put("originX", 0.0)
				.put("originY", originY)
				.put("templateId", templateId)
				.put("disconnectedNodeAcknowledged", true);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE.toString());
		client.setHeaders(headers);
		client.post(serverUrl+FileUtils.getPathWithReplaceValue(NiFiApiURLConstants.TEMPLATE_INSTANTIATES, "id", processGroupId),json.toString());
		if(!isService)
		  templateDetailsService.updateTemplateDetails(new JSONObject().put("originY", originY), templateDetailsId);
	}

	private Double getOriginY(String lob) {
		Double maxOriginY = templateDetailsDAO.getMaxOriginY(lob);
		return maxOriginY+1500.00;
	}

	private void uploadServices(TemplateKeys templateKey,JSONObject templateData, String processGroupId, String serverUrl, String lob, String templateDetailsId) {
		for(String service : templateKey.getServices()) {
			setServiceTemplateName(templateData,service,templateKey.getSqlType());
			String templateString = prepareTemplate("services/"+service,templateData);
			try {
				String templateId = templateHelper.getTemplateIdFromResponse(uploadTemplate(templateString,processGroupId,serverUrl));
				intanstiateTemplate(processGroupId, templateId, serverUrl, lob, templateDetailsId,true);
			} catch (Exception e) {
				System.err.print(e.getMessage());
			}
		}
	}

	private void setServiceTemplateName(JSONObject templateData, String service, String sqlType) {
       String serviceTemplateName = service.split("\\.")[0];
       if(sqlType!=null)
    	   serviceTemplateName+="("+sqlType+")";
		templateData.put("Template_Name", serviceTemplateName);	
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
				templateString = templateString.replaceFirst(argument, templateData.optString(argument.replaceAll(REPLACE, "")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
}

package com.applicate.nifiui.helper;

import static com.applicate.nifiui.config.constants.ConnectionConstants.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.applicate.nifiui.configuration.provider.GlobalConfigurationProvider;
import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.applicate.nifiui.dbmanager.dao.beans.TemplateDetails;
import com.applicate.nifiui.service.ConnectionMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TemplateHelper {
	
	private final Map<TemplateKeys,String> templateRegistry = new HashMap<TemplateKeys, String>();
	
	public enum TemplateKeys{
		SFTP_TO_MYSQL("DBCPConnectionPoolService.xml","CSVReader.xml","JsonRecordSetWriter.xml"),
		SFTP_TO_CLICKHOUSE("DBCPConnectionPoolService.xml","CSVReader.xml","JsonRecordSetWriter.xml");
		
		private String[] services;
		
		private 
		
		TemplateKeys(String... services) {
			this.services = services;
		}
		
		public String[] getServices(){
		  return this.services;
		}
		
		public String getSqlType(){
			for(String str : this.toString().split("_")) {
				if(SQL_TYPE.contains(str))
					return str;
			}
		  return null;
		}
		
	}
	
	@Autowired
	private GlobalConfigurationProvider provider;
	
	@Autowired
	private ConnectionMapperService connectionMapperService;
	
	private ObjectMapper om = new ObjectMapper();
	
	@PostConstruct
	public void loadTemplate() {
		templateRegistry.put(TemplateKeys.SFTP_TO_MYSQL, "csvToSql.xml");
		templateRegistry.put(TemplateKeys.SFTP_TO_CLICKHOUSE, "csvToSql.xml");
	}
	
	public String getTemplate(String templateKey) {
		return templateRegistry.get(TemplateKeys.valueOf(templateKey.toUpperCase()));
	}
   
	public JSONObject getReplacebleValueAsJSON(TemplateDetails templateDetails, Connection extractor, Connection loader) throws JSONException, JsonProcessingException {
		JSONObject extractJSON = getDataWithActualParam(extractor),loaderJSON = getDataWithActualParam(loader);
		String processorGroupId = provider.get(loader.getLob()).getStringConstants("Processor_Group_Id");
		return new JSONObject().put("Processor_Group_Id", processorGroupId)
		              .put("Template_Name", generateTemplateName(extractor.getType(),loader.getType(),templateDetails.getLoaderTable()))
		              .put("Loader_"+DB_URL, loaderJSON.getString(DB_URL))
		              .put("Loader_"+DRIVER_CLASS, loaderJSON.getString(DRIVER_CLASS))
		              .put("Loader_"+DRIVER_LOCATION, loaderJSON.getString(DRIVER_LOCATION))
		              .put("Loader_"+USER_NAME, loaderJSON.getString(USER_NAME))
		              .put("Loader_"+PASSWORD, loaderJSON.getString(PASSWORD))
		              .put("Query_Type", "INSERT")
		              .put("loaderTable", templateDetails.getLoaderTable())
		              .put("Extractor_"+HOST, extractJSON.getString(HOST))
		              .put("Extractor_"+PORT, extractJSON.getString(PORT))
		              .put("Extractor_"+USER_NAME, extractJSON.getString(USER_NAME))
		              .put("Extractor_"+PASSWORD, extractJSON.getString(PASSWORD))
		              .put("extractorTable", templateDetails.getExtractorTable())
		              .put("FilePath", extractJSON.getString(FILE_PATH))
		              .put("Scheduling_Period", "3600")
		              .put("mapping", templateDetails.getMapping())
		              .put("mappingKey", templateDetails.getLoaderTable());
	}

	public JSONObject getDataWithActualParam(Connection connection) throws JSONException, JsonProcessingException {
		return connectionMapperService.getUnWrappedConnection(new JSONObject(om.writeValueAsString(connection)), connection.getLob(), connection.getType());
	}

	public String generateTemplateName(String extractorType, String LoaderType, String loaderTable) {
		return extractorType.toUpperCase()+"_TO_"+LoaderType.toUpperCase()+"("+loaderTable+")";
	}
	
	public String getTemplateIdFromResponse(String response) {
		return response.split("<id>")[1].split("</id>")[0];
	}
	
}

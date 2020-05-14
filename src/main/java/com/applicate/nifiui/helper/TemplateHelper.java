package com.applicate.nifiui.helper;

import static com.applicate.nifiui.config.constants.ConnectionConstants.SQL_TYPE;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
		SFTP_TO_MYSQL("DBCPConnectionPoolLoader.xml","CSVReader.xml","JsonRecordSetWriter.xml"),
		SFTP_TO_CLICKHOUSE("DBCPConnectionPoolLoader.xml","CSVReader.xml","JsonRecordSetWriter.xml"),
		CLICKHOUSE_TO_CLICKHOUSE("DBCPConnectionPoolExtractor.xml","AVROReader.xml","JsonRecordSetWriter.xml","DBCPConnectionPoolLoader.xml"),
		MYSQL_TO_CLICKHOUSE("DBCPConnectionPoolExtractor.xml","AVROReader.xml","JsonRecordSetWriter.xml","DBCPConnectionPoolLoader.xml"),
		CLICKHOUSE_TO_MYSQL("DBCPConnectionPoolExtractor.xml","AVROReader.xml","JsonRecordSetWriter.xml","DBCPConnectionPoolLoader.xml"),
		MYSQL_TO_MYSQL("DBCPConnectionPoolExtractor.xml","AVROReader.xml","JsonRecordSetWriter.xml","DBCPConnectionPoolLoader.xml"),
		MONGO_TO_CLICKHOUSE("DBCPConnectionPoolExtractor.xml","AVROReader.xml","JsonRecordSetWriter.xml","DBCPConnectionPoolLoader.xml");
		
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
	
	private static ObjectMapper om = new ObjectMapper();
	
	@PostConstruct
	public void loadTemplate() {
		templateRegistry.put(TemplateKeys.SFTP_TO_MYSQL, "csvToSql.xml");
		templateRegistry.put(TemplateKeys.SFTP_TO_CLICKHOUSE, "csvToSql.xml");
		templateRegistry.put(TemplateKeys.CLICKHOUSE_TO_CLICKHOUSE, "sqltosql.xml");
		templateRegistry.put(TemplateKeys.MYSQL_TO_CLICKHOUSE, "sqltosql.xml");
		templateRegistry.put(TemplateKeys.CLICKHOUSE_TO_MYSQL, "sqltosql.xml");
		templateRegistry.put(TemplateKeys.MYSQL_TO_MYSQL, "sqltosql.xml");
		templateRegistry.put(TemplateKeys.MONGO_TO_CLICKHOUSE, "nosqltosql.xml");
	}
	
	public String getTemplate(String templateKey) {
		return templateRegistry.get(TemplateKeys.valueOf(templateKey.toUpperCase()));
	}
   
	public JSONObject getReplacebleValueAsJSON(TemplateDetails templateDetails, Connection extractor, Connection loader) throws JSONException, JsonProcessingException {
	//JSONObject extractJSON = getDataWithActualParam(extractor),loaderJSON = getDataWithActualParam(loader);
		JSONObject json = new JSONObject();
		putConnection("Extractor_",extractor,json);
		putConnection("Loader_",loader,json);
		String processorGroupId = provider.get(loader.getLob()).getStringConstants("Processor_Group_Id");
		return json.put("Processor_Group_Id", processorGroupId)
		              .put("Template_Name", generateTemplateName(extractor.getType(),loader.getType(),templateDetails.getLoaderTable()))
		              .put("Query_Type", "INSERT")
		              .put("loaderTable", templateDetails.getLoaderTable())		              
		              .put("extractorTable", templateDetails.getExtractorTable())		            
		              .put("Scheduling_Period", "3600")
		              .put("mapping", templateDetails.getMapping())
		              .put("mappingKey", templateDetails.getLoaderTable())
		              .put("validation",templateDetails.getValidation());
		         
	}

	private String getExtractorTable(Connection extractor, TemplateDetails templateDetails) {
		return extractor.getType().equalsIgnoreCase("sftp")?extractor.getParam5()+"/"+templateDetails.getExtractorTable():templateDetails.getExtractorTable();
	}

	private void putConnection(String appendValue, Connection connection, JSONObject json) throws JsonProcessingException {
		JSONObject connectionJson = new JSONObject(om.writeValueAsString(connection));
		for (String key : JSONObject.getNames(connectionJson)) {
			if(key.startsWith("param") || key.equals("type"))
			json.put(appendValue+key, connectionJson.optString(key));
		}
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

package com.applicate.nifiui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.applicate.nifiui.dbmanager.dao.beans.NiFiSchema;
import com.applicate.nifiui.dbmanager.dao.dboperation.NiFiSchemaDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NiFiSchemaServices {

	private Logger log = LoggerFactory.getLogger(NiFiSchemaServices.class);

	private ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private NiFiSchemaDAO schemaDAO;
	
	@Autowired
	private SessionServices sessionServices;

	public String persistSchema(MultipartFile file) {
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			String sheetName = workbook.getSheetName(0);
			Sheet sheet = workbook.getSheet(sheetName);
			Row row = sheet.getRow(0);
			JSONObject schemaJosn = new JSONObject().put("schemaName", sheetName).put("lob", sessionServices.getLob());
			for (Cell cell : row) {
				schemaJosn.put("columnName", cell.getStringCellValue());
				NiFiSchema schema = om.readValue(schemaJosn.toString(), NiFiSchema.class);
				schemaDAO.save(schema);
			}
		} catch (InvalidFormatException | IOException e) {
			log.error(e.getMessage(),e);
		}
		return "saved";
	}

	public String getSchema(String lob,String tableName) {
		lob = lob!=null ? lob : sessionServices.getLob();
		List<NiFiSchema> ls = new ArrayList<NiFiSchema>();
		if(tableName!=null) {
			ls = schemaDAO.getNiFiSchemaBasedOnSchemaNameAndLob(lob, tableName);
		} else { 
			ls = schemaDAO.getNiFiSchemaBasedOnLob(lob); 
		}
		return getJSONArray(ls).toString();
	}

//	private String getSchemaData(List<NiFiSchema> ls) {
//		StringBuilder response = new StringBuilder("{");
//		String finalResponse=""; 
//		if(!ls.isEmpty()) {
//			String tempCollectionName=ls.get(0).getSchemaName();
//			response.append("\""+tempCollectionName+"\":[");
//			for (NiFiSchema niFiSchema : ls) {
//				if(niFiSchema.getSchemaName().equals(tempCollectionName))
//				    response.append("\""+niFiSchema.getColumnName()+"\",");
//				else {
//					response.replace(response.length()-1, response.length(), "");
//					tempCollectionName=niFiSchema.getSchemaName();
//					response.append("],\""+tempCollectionName+"\":[");
//					response.append("\""+niFiSchema.getColumnName()+"\",");
//				}
//			}
//			finalResponse = response.substring(0, response.length()-1);
//		}
//		return finalResponse+"]}";
//	}

	public String getTableNames(Optional<String> lob) {
		String clientName = lob.isPresent()?lob.get():sessionServices.getLob();
		JSONArray newList = new JSONArray();
		List<String> tableNames = schemaDAO.getTableNames(clientName);
		for (String tableName : tableNames) {
			newList.put(new JSONObject().put("schemaName", tableName));
		}
		return newList.toString();
	}

	private JSONArray getJSONArray(List<NiFiSchema> ls) {
		JSONArray newList = new JSONArray();
		for (NiFiSchema niFiSchema :  ls) {
			try {
				newList.put(new JSONObject(om.writeValueAsString(niFiSchema)));
			}catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		return newList;
	}
}

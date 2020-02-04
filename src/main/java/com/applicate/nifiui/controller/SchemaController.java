package com.applicate.nifiui.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.applicate.nifiui.service.NiFiSchemaServices;
import com.applicate.utils.ExcelUtils;

@RestController
@RequestMapping("/schema")
public class SchemaController {
	
	@Autowired
	private NiFiSchemaServices schemaServices;
	
	@RequestMapping(value="/uploadSchema", method=RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String uploadSchema(@RequestParam("schema") MultipartFile file){
		if(ExcelUtils.supportedContentTypes.contains(file.getContentType())) {
		   return "Connection Created<<<"+schemaServices.persistSchema(file).toString()+">>>";
		}
		return "You can only upload Excel File";
	}
	
	@RequestMapping(value= {"/getSchema/{tableName}","/getSchema/{lob}/{tableName}","/getSchema"}, method=RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String getSchema(@PathVariable("tableName")Optional<String> tableName,@PathVariable("lob")Optional<String> lob){
		String collectionName=null,tempLob=null;
		if(tableName.isPresent())
			collectionName = tableName.get();
		if(lob.isPresent())
			tempLob = lob.get();
		return schemaServices.getSchema(tempLob,collectionName);
	}
	
	@RequestMapping(value= {"/getTableNames/{lob}","/getTableNames"}, method=RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String getTableNames(@PathVariable("lob")Optional<String> lob){
		return schemaServices.getTableNames(lob);
	}
	
	

}

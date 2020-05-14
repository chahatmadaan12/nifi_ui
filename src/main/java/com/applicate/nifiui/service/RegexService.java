package com.applicate.nifiui.service;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.applicate.nifiui.dbmanager.dao.beans.Regex;
import com.applicate.nifiui.dbmanager.dao.dboperation.RegexDAO;
import com.applicate.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RegexService {
	
	private Logger log = LoggerFactory.getLogger(RegexService.class);
	
	@Autowired
	private RegexDAO regexDAO;
	
	@Autowired
	private SessionServices sessionServices;

	private static ObjectMapper om = new ObjectMapper();

	public String getRegex(String lob) {
	    String client = lob!=null?lob:sessionServices.getLob();
	    if(!StringUtils.isEmpty(client)) {
	     return regexDAO.getRegexBasedOnLob(client).toString();
	    }
		return regexDAO.findAll().toString();
	}

	public Object persistRegex(JSONArray regexArray) {
		StringBuilder response=new StringBuilder("[");
		for (Object regex : regexArray) {
			try {
				Regex regexObj = om.readValue(regex.toString(), Regex.class);
				regexObj.setLob(sessionServices.getLob());
				regexObj.setId(regexObj.getLob()+"_"+regexObj.getName());
				response.append(regexDAO.save(regexObj).toString());
			} catch (JsonProcessingException e) {
				log.error("Exception happend while reading regex object for persistence");
			}
		}
		return response.append("]");
	}

	
}

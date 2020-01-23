package com.applicate.nifiui.service;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.applicate.nifiui.dbmanager.dao.beans.TemplateDetails;
import com.applicate.nifiui.dbmanager.dao.dboperation.TemplateDetailsDAO;
import com.applicate.utils.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
@Service
public class TemplateDetailsService {
	
	@Autowired
	private SessionServices sessionServices;
	
	@Autowired
	private TemplateDetailsDAO templateDetailsDAO;

	private ObjectMapper om = new ObjectMapper();
	
	public TemplateDetails persistTemplateDetails(JSONObject templateDetails){
		String lob = sessionServices.getLob();
		templateDetails.put("id",UUID.randomUUID().toString());
		templateDetails.put("lob", lob);
		TemplateDetails obj=null;
		try {
			obj = om.readValue(templateDetails.toString(), TemplateDetails.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return templateDetailsDAO.save(obj);
	}

	public String updateTemplateDetails(JSONObject jsonObject, String id) {
		List<TemplateDetails> ls = templateDetailsDAO.getTemplateDetailsIfPersent(templateDetailsDAO, id);
		TemplateDetails templateDetails = !ls.isEmpty()?ls.get(0):new TemplateDetails();
		if(templateDetails.getLob()==null) {
			return id+" not found";
		}
		TemplateDetails obj=null;
		try {
			JSONObject tempJson = new JSONObject(om.writeValueAsString(templateDetails));
			obj = om.readValue(JSONUtils.mergeJSON(tempJson,jsonObject).toString(), TemplateDetails.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return templateDetailsDAO.save(obj).toString();
	}

	public String deleteTemplateDetails(String id) {
		templateDetailsDAO.deleteById(id);
		return id+" deleted";
	}

}

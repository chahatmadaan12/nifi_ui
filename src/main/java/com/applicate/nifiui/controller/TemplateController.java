package com.applicate.nifiui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.applicate.nifiui.service.NiFiTemplateService;

@RestController
@RequestMapping("/template")
public class TemplateController {
	
	@Autowired
	private NiFiTemplateService niFiTemplateService; 
	
	@RequestMapping(value = {"/upload/{id}"}, method = RequestMethod.GET ,produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody String uploadTemplate(@PathVariable(name = "id")String id) {
		return niFiTemplateService.createAndUpload(id);
	}
	
}

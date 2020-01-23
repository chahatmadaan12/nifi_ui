package com.applicate.nifiui.controller;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.applicate.nifiui.service.TemplateDetailsService;

@RestController
@RequestMapping("/templateDetails")
public class TemplateDetailsController {
	
	@Autowired
	private TemplateDetailsService templateDetailsService;

	@RequestMapping(value = {"/put","/put/{id}"},method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String putTemplateDetails(@RequestBody String json,@PathVariable(value = "id") Optional<String> id) {
		if(id.isPresent())
			return templateDetailsService.updateTemplateDetails(new JSONObject(json),id.get()).toString();
		return templateDetailsService.persistTemplateDetails(new JSONObject(json)).toString();
	}
	
	@RequestMapping(value = "/delete/{id}",method = RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String deleteTemplateDetails(@PathVariable(value = "id") String id) {
		return templateDetailsService.deleteTemplateDetails(id);
	}
	
}

package com.applicate.nifiui.controller;

import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.applicate.nifiui.service.RegexService;
import com.applicate.utils.JSONUtils;

@RestController
@RequestMapping("/regex")
public class RegexController {
	
	@Autowired
	private RegexService regexService;
	
	@RequestMapping(value= {"/getRegex/{lob}","/getRegex"},method=RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
	public String getRegex(@PathVariable(value="lob")Optional<String> lob){ 
		if(lob.isPresent()) {
			return regexService.getRegex(lob.get());
		}
		return regexService.getRegex(null);
	}
	
	@RequestMapping(value="/putRegex", method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String putRegex(@RequestBody String json){
		JSONArray regexArray = JSONUtils.getJSONArray(json);
		return regexService.persistRegex(regexArray).toString();
	}
	
}

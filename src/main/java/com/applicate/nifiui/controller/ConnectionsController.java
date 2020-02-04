package com.applicate.nifiui.controller;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.applicate.nifiui.service.ConnectionsControllerService;

@RestController
@RequestMapping("/connection")
public class ConnectionsController{

	@Autowired
	private ConnectionsControllerService connectionsControllerService;

	@RequestMapping(value= {"","^((?!/getConnections|/putConnection).)*$"})
	public ResponseEntity<String> connenctionDefault() {
		return new ResponseEntity<String>("Service Unavailable",HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@RequestMapping(value= {"/getConnections/{lob}","/getConnections"},method=RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
	public String getConnections(@PathVariable(value="id")Optional<String> id,@PathVariable(value="lob")Optional<String> lob){ 
		String response = "";
		if(id.isPresent()&&lob.isPresent())
			response = /*"{\"Connections\":"+*/connectionsControllerService.getConnections(id.get(),lob.get()).toString()/*+"}"*/;
		else if(id.isPresent()) 
			response = /* "{\"Connections\":"+ */connectionsControllerService.getConnections(id.get(),null).toString()/*+"}"*/;
		else if(lob.isPresent())
			response = /* "{\"Connections\":"+ */connectionsControllerService.getConnections(null, lob.get()).toString()/* +"}" */;
		else
			response = /* "{\"Connections\":" +*/connectionsControllerService.getConnections(null, null).toString()/* +"}" */;
		return response;
	}
	
	@RequestMapping(value= {"/getVerifiedConnections/{lob}"},method=RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
	public String getVerifiedConnections(@PathVariable(value="lob")Optional<String> lob){ 
		String response = "";
		if(lob.isPresent())
			response = connectionsControllerService.getVerifiedConnections(lob.get()).toString();
		return response;
	}

	@RequestMapping(value="/putConnection", method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String putConnections(@RequestBody String json){
		return connectionsControllerService.persistConnection(new JSONObject(json)).toString();
	}
	
	@RequestMapping(value="/putConnection/{id}", method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String putConnections(@RequestBody String json,@PathVariable(value="id") String id){
		return "<<<"+connectionsControllerService.updateConnection(new JSONObject(json),id).toString()+">>>";
	}

	@RequestMapping(value="/verifyConnection/{id}",method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> verifyConnection(@PathVariable(value="id") String id){ 
		return connectionsControllerService.verifyConnection(id);
	}
	
	@RequestMapping(value="/deleteConnection/{id}", method=RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String deleteConnections(@PathVariable(value="id") String id){
		return "<<<"+connectionsControllerService.deleteConnection(id).toString()+">>>";
	}

}

package com.applicate.nifiui.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.applicate.nifiui.configurations.BeanConfig;
import com.applicate.nifiui.configurations.ConnectionConfiguration.Connections;
import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.applicate.nifiui.dbmanager.dao.dboperation.ConnectionDAO;
import com.applicate.nifiui.service.ConnectionVerifierFactory;

@RestController
@RequestMapping("/connection")
public class ConnectionsController{

	AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	@Autowired
	private ConnectionDAO connectionDAO;
	@Autowired
	private ConnectionVerifierFactory connectionVerifierFactory;
	Connections con=null;

	@RequestMapping(value= {"","^((?!/getConnections|/putConnection).)*$"})
	public ResponseEntity<String> connenctionDefault() {
		return new ResponseEntity<String>("Service Unavailable",HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@RequestMapping(value="/getConnections",method=RequestMethod.GET,produces = MediaType.TEXT_PLAIN_VALUE)
	public String getConnections(){ 
		if(con==null) {
			return "No Connection Found";
		}
		return con.getConnection();
	}

	@RequestMapping(value="/putConnection", method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String putConnections(@RequestBody String json){
		con=(Connections)ctx.getBean("sftp");
		con.setId("sftp1");
		con.setParam1("param1");
		con.setParam2("param2");
		con.setParam3("param3");
		con.setParam4("param4");
		con.setParam5("param5");
		con.setParam6("param6");
		con.setParam7("param7");
		return "Connection Created<<<"+json+">>>";
	}

	@RequestMapping(value="/verifyConnection/{id}",method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> verifyConnection(@PathVariable(value="id") String id){ 
		Connection connection = connectionDAO.findById(id).get();
		boolean verify=false;
		try {
			verify = connectionVerifierFactory.getConnectionVerifier(connection.getType()).verify(connection);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new JSONObject().put("verify", verify).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new JSONObject().put("verify", verify).toString(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/verifyConnections/{lob}",method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> verifyConnections(@PathVariable(value="lob") String lob){ 
        List<Connection> connectionList = connectionDAO.getConnectionBasedOnLob(lob);
        JSONObject response = new JSONObject();
        for (Connection connection : connectionList) {
        	try {
        		response.put(connection.getId(), new JSONObject().put("verify",  connectionVerifierFactory.getConnectionVerifier(connection.getType()).verify(connection)));
			} catch (Exception e) {
				e.printStackTrace();
			    response.put(connection.getId(), new JSONObject().put("verify", false).put("message", e.getMessage()));
			}
		}
		return new ResponseEntity<Object>(response.toString(),HttpStatus.OK);
	}

}

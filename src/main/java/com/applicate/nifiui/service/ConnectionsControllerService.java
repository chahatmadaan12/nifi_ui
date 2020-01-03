package com.applicate.nifiui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.applicate.nifiui.dbmanager.dao.dboperation.ConnectionDAO;
import com.applicate.utils.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConnectionsControllerService {
	
	@Autowired
	private ConnectionMapperService connectionMapperService;
	
	@Autowired
	private ConnectionDAO connectionDAO;
	
	private ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private ConnectionVerifierFactoryService connectionVerifierFactory;
	
	public Connection persistConnection(JSONObject connObject){
		JSONObject wrappedConnection = connectionMapperService.getWrappedConnection(connObject, connObject.getString("lob"), connObject.getString("type"));
		wrappedConnection.put("id",UUID.randomUUID().toString());
		Connection obj=null;
		try {
			obj = om.readValue(wrappedConnection.toString(), Connection.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return connectionDAO.save(obj);
	}
	
	public List<JSONObject> getConnections(String id,String lob) {
		List<Connection> ls = new ArrayList<Connection>();
		if(id!=null)
		    ls = connectionDAO.getConnectionIfPersent(connectionDAO, id);
		else if(lob!=null)
			ls=connectionDAO.getConnectionBasedOnLob(lob);
		else
			ls=(List<Connection>) connectionDAO.findAll();
		return getMappedData(ls);
	}
	
	private List<JSONObject> getMappedData(List<Connection> ls) {
		List<JSONObject> newList = new ArrayList<JSONObject>();
		for (Connection connection : ls) {
			try {
				JSONObject json = new JSONObject(om.writeValueAsString(connection));
				newList.add(connectionMapperService.getUnWrappedConnection(json, connection.getLob(), connection.getType()));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return newList;
	}
	
	public ResponseEntity<Object> verifyConnection(String id){
		List<Connection> ls = connectionDAO.getConnectionIfPersent(connectionDAO, id);
		Connection connection = !ls.isEmpty()?ls.get(0):new Connection();
		boolean verify=false;
		if(connection.getLob()==null) {
			return new ResponseEntity<>(new JSONObject().put("verify", verify).put("message", id+" not found").toString(), HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			JSONObject connJson = new JSONObject(om.writeValueAsString(connection));
			JSONObject unWrappedConnection = connectionMapperService.getUnWrappedConnection(connJson, connection.getLob(), connection.getType());
			verify = connectionVerifierFactory.getConnectionVerifier(connection.getType()).verify(unWrappedConnection);
			if(verify) {
				connection.setActive(true);
				connectionDAO.save(connection);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new JSONObject().put("verify", verify).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new JSONObject().put("verify", verify).toString(), HttpStatus.OK);
	}
	
	public String updateConnection(JSONObject json,String id) {
		List<Connection> ls = connectionDAO.getConnectionIfPersent(connectionDAO, id);
		Connection connection = !ls.isEmpty()?ls.get(0):new Connection();
		if(connection.getLob()==null) {
			return id+" not found";
		}
		JSONObject wrappedConnection = connectionMapperService.getWrappedConnection(json, connection.getLob(), connection.getType());
		Connection obj=null;
		try {
			JSONObject connJson = new JSONObject(om.writeValueAsString(connection));
			wrappedConnection = JSONUtils.mergeJSON(connJson,wrappedConnection);
			obj = om.readValue(wrappedConnection.toString(), Connection.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return connectionDAO.save(obj).toString();
	}

	public String deleteConnection(String id) {
		connectionDAO.deleteById(id);
		return id+" deleted";
	}
}

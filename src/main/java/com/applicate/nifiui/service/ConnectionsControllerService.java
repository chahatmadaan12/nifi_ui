package com.applicate.nifiui.service;

import static com.applicate.nifiui.config.constants.ConnectionConstants.DB_URL;
import static com.applicate.nifiui.config.constants.ConnectionConstants.DRIVER_CLASS;
import static com.applicate.nifiui.config.constants.ConnectionConstants.DRIVER_JAR_KEY;
import static com.applicate.nifiui.config.constants.ConnectionConstants.DRIVER_LOCATION;
import static com.applicate.nifiui.config.constants.ConnectionConstants.SQL_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.applicate.nifiui.configuration.provider.GlobalConfigurationProvider;
import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.applicate.nifiui.dbmanager.dao.dboperation.ConnectionDAO;
import com.applicate.utils.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConnectionsControllerService {
	
	private Logger log = LoggerFactory.getLogger(ConnectionsControllerService.class);
	
	@Autowired
	private ConnectionMapperService connectionMapperService;
	
	@Autowired
	private ConnectionDAO connectionDAO;
	
	@Autowired
	private SessionServices sessionServices;
	
	private ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private GlobalConfigurationProvider provider;
	
	@Autowired
	private ConnectionVerifierFactoryService connectionVerifierFactory;
	
	public Connection persistConnection(JSONObject connObject) {
		String lob = sessionServices.getLob(), type = connObject.getString("type");
		Connection obj = null;
		try {
			putSqlSpecificFields(connObject, type, lob);
			JSONObject wrappedConnection = connectionMapperService.getWrappedConnection(connObject, lob, type);
			wrappedConnection.put("id", UUID.randomUUID().toString());
			wrappedConnection.put("lob", lob);
			obj = om.readValue(wrappedConnection.toString(), Connection.class);
		} catch (Exception e) {
			log.error("Exception happend while persisting the connection",e);
		}
		log.info("connections persisted for "+lob);
		return connectionDAO.save(obj);
	}

	private void putSqlSpecificFields(JSONObject connection, String type, String lob) throws Exception {
		if(SQL_TYPE.contains(type.toUpperCase())) {
		  JSONObject verify = connectionVerifierFactory.getConnectionVerifier(type).verify(connection);
		  connection.put(DB_URL, verify.getString(DB_URL)).put(DRIVER_CLASS, verify.getString(DRIVER_CLASS)).put(DRIVER_LOCATION, getDriverLoaction(lob,verify.getString(DRIVER_JAR_KEY)));
		}
	}

	private String getDriverLoaction(String lob, String driverKey) {
		String nifi_home = System.getenv("NIFI_HOME");
		return nifi_home+"/lib/"+provider.get(lob).getStringConstants(driverKey);
	}

	public List<JSONObject> getConnections(String id,String lob) {
		lob = lob!=null ? lob : sessionServices.getLob();
		List<Connection> ls = new ArrayList<Connection>();
		if(id!=null)
		    ls = connectionDAO.getConnectionIfPersent(connectionDAO, id);
		else if(lob!=null)
			ls=connectionDAO.getConnectionBasedOnLob(lob);
		else
			ls=(List<Connection>) connectionDAO.findAll();
		log.info("got connections for "+lob);
		return getMappedData(ls);
	}
	
	public List<JSONObject> getVerifiedConnections(String lob) {
		lob = lob!=null ? lob : sessionServices.getLob();
		List<Connection> ls = new ArrayList<Connection>();
		if(lob!=null)
			ls=connectionDAO.getVerifiedConnection(lob);
		log.info("got verified connections for "+lob);
		return getMappedData(ls);
	}
	
	private List<JSONObject> getMappedData(List<Connection> ls) {
		List<JSONObject> newList = new ArrayList<JSONObject>();
		for (Connection connection : ls) {
			try {
				JSONObject json = new JSONObject(om.writeValueAsString(connection));
				newList.add(connectionMapperService.getUnWrappedConnection(json, connection.getLob(), connection.getType()));
			} catch (JsonProcessingException e) {
				log.error("Exception happend while wrapping the connection",e);
			}
		}
		return newList;
	}
	
	public ResponseEntity<Object> verifyConnection(String id){
		List<Connection> ls = connectionDAO.getConnectionIfPersent(connectionDAO, id);
		Connection connection = !ls.isEmpty()?ls.get(0):new Connection();
		JSONObject verify= new JSONObject();
		if(connection.getLob()==null) {
			return new ResponseEntity<>(new JSONObject().put("verify", false).put("message", id+" not found").toString(), HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			JSONObject connJson = new JSONObject(om.writeValueAsString(connection));
			JSONObject unWrappedConnection = connectionMapperService.getUnWrappedConnection(connJson, connection.getLob(), connection.getType());
			verify = connectionVerifierFactory.getConnectionVerifier(connection.getType()).verify(unWrappedConnection);
			if(verify.getBoolean("verify")) {
				connection.setActive(true);
				connectionDAO.save(connection);
			}
		} catch (Exception e) {
			log.error("Exception happend while verifing the connection",e);
			return new ResponseEntity<>(new JSONObject().put("verify", false).put("message", e.getMessage()).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new JSONObject().put("verify", verify.getBoolean("verify")).toString(), HttpStatus.OK);
	}
	
	public String updateConnection(JSONObject json,String id) {
		List<Connection> ls = connectionDAO.getConnectionIfPersent(connectionDAO, id);
		Connection connection = !ls.isEmpty()?ls.get(0):new Connection();
		if(connection.getLob()==null) {
			return id+" not found";
		}
		Connection obj=null;
		try {
			connection.setActive(false);
			putSqlSpecificFields(json, connection.getType(), connection.getLob());
			JSONObject wrappedConnection = connectionMapperService.getWrappedConnection(json, connection.getLob(), connection.getType());
			JSONObject connJson = new JSONObject(om.writeValueAsString(connection));
			wrappedConnection = JSONUtils.mergeJSON(connJson,wrappedConnection);
			obj = om.readValue(wrappedConnection.toString(), Connection.class);
		} catch (Exception e) {
			log.error("Exception happend while updating the connection",e);
		}
		log.info("connections updated for connection id "+id);
		return connectionDAO.save(obj).toString();
	}

	public String deleteConnection(String id) {
		connectionDAO.deleteById(id);
		return id+" deleted";
	}
}

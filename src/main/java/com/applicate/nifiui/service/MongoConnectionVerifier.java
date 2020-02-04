package com.applicate.nifiui.service;
import static com.applicate.nifiui.config.constants.ConnectionConstants.DB_NAME;
import static com.applicate.nifiui.config.constants.ConnectionConstants.HOST;
import static com.applicate.nifiui.config.constants.ConnectionConstants.PASSWORD;
import static com.applicate.nifiui.config.constants.ConnectionConstants.PORT;
import static com.applicate.nifiui.config.constants.ConnectionConstants.USER_NAME;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.applicate.utils.StringUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;


@Component
public class MongoConnectionVerifier implements ConnectionVerificationService{

	@Override
	public JSONObject verify(JSONObject connection) throws Exception {
		String userName = connection.getString(USER_NAME),password = connection.getString(PASSWORD);
	        MongoClient client = null;
	        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
	            MongoCredential credentials = MongoCredential.createCredential(userName, connection.getString(DB_NAME), password.toCharArray());
	            client = new MongoClient(connection.getString(HOST), Integer.parseInt(connection.getString(PORT)));
	            client.getCredential();
	        }
	        else {
	            client = new MongoClient(connection.getString(HOST), Integer.parseInt(connection.getString(PORT)));
	        }
	        MongoDatabase db = client.getDatabase(connection.getString(DB_NAME));
	        String collection = db.listCollectionNames().first();
	        client.close();
	        JSONObject response = new JSONObject();
	        response = StringUtils.isEmpty(collection)?response.put("verify", false):response.put("verify", true);
	        return response;
	}

}

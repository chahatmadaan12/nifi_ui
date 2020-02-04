package com.applicate.nifiui.service;

import org.json.JSONObject;

public interface ConnectionVerificationService {
	
	JSONObject verify(JSONObject connection) throws Exception;

}

package com.applicate.nifiui.service;

import org.json.JSONObject;

public interface ConnectionVerificationService {
	
	boolean verify(JSONObject unWrappedConnection) throws Exception;

}

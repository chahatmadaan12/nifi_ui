package com.applicate.nifiui.configuration.provider;

import org.json.JSONObject;

public interface ConfigurationProvider {

	JSONObject getConnectionParam(String type);
	
	JSONObject getConnectionParam();
	
}

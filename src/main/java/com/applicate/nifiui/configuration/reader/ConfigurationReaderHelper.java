package com.applicate.nifiui.configuration.reader;

import java.util.Properties;

import org.json.JSONObject;

public class ConfigurationReaderHelper {
	
    public ConfigurationReader<JSONObject> createCommonJsonReader(String title, String filePath) {
        return ConfigurationReaderFactory.createConfigurationReader(title,JSONObject.class, filePath,true, true);
    }
    
    public ConfigurationReader<Properties> createCommonPropertyReader(String title, String filePath) {
    	  return ConfigurationReaderFactory.createConfigurationReader(title,Properties.class, filePath,true, true);
    }

    public ConfigurationReader<String> createCommonStringReader(String title, String filePath) {
    	  return ConfigurationReaderFactory.createConfigurationReader(title,JSONObject.class, filePath,true, true);
    }

}

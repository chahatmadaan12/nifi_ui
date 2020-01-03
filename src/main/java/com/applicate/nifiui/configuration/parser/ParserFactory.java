package com.applicate.nifiui.configuration.parser;

import java.io.File;
import java.util.Properties;

import org.json.JSONObject;

public class ParserFactory {

	    private ParserFactory() {
	        throw new UnsupportedOperationException("This operation is not allowed for this constructor");
	    }

	    public static Parser<JSONObject> getJsonParser(String fileName) {
	        return new JsonParser<JSONObject>(fileName);
	    }

	    public static Parser<JSONObject> getJsonParser(File file) {
	        return new JsonParser<JSONObject>(file);
	    }

	    public static Parser<Properties> getPropertiesParser(String fileName) {
	        return new PropertiesParser<Properties>(fileName);
	    }

	    public static Parser<Properties> getPropertiesParser(File file) {
	        return new PropertiesParser<Properties>(file);
	    }

	    public static Parser getParser(String fileName) {
	        return getParser(new File(fileName));
	    }

	    public static Parser<JSONObject> getYamlParser(File file) {
	        return new YamlParser<JSONObject>(file);
	    }

	    public static Parser getParser(File file) {
	        String fileName = file.getName();
	        if (fileName.endsWith(".properties")) {
	            return new PropertiesParser(file);
	        }
	        if (fileName.endsWith(".json")) {
	            return new JsonParser(file);
	        }
	        if (fileName.endsWith(".yaml")) {
	            return new YamlParser<>(file);
	        }
	        throw new RuntimeException("Given file name not valid");
	    }
}

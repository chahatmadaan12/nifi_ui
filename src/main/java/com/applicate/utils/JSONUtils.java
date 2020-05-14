package com.applicate.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class JSONUtils {

	public static String yamlToJson(String yaml) throws Exception {
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		Object obj = yamlReader.readValue(yaml, Object.class);
		ObjectMapper jsonWriter = new ObjectMapper();
		return jsonWriter.writeValueAsString(obj);
	}

	public static JSONObject cloneJSONObject(JSONObject content) {
		try {
			return new JSONObject(content.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String jsonToYaml(String jsonString) throws Exception {
		JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
		return new YAMLMapper().writeValueAsString(jsonNodeTree);
	}

	public static Object getEmptyObject() {
		try {
			return new JSONObject();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    public static JSONObject safeClone(JSONObject itemToClone) {
        if (itemToClone != null) {
            try {
                return new JSONObject(itemToClone.toString());
            } catch (JSONException e) {
            }
        }
        return null;
    }
    
    public static boolean containsData(JSONObject jsonObject) {
        return !isEmpty(jsonObject);
    }
    
    public static boolean isEmpty(JSONObject jsonObject) {
        return (jsonObject == null) || (jsonObject.length() < 1);
    }
    
    /**
     * this for replacing key as value and vice versa
     * @param json must contain key and value in String
     * @return JSONObject
     */
    public static JSONObject getKeyAsValue(JSONObject json) {
    	JSONObject newJson = new JSONObject();
		String[] keys = JSONObject.getNames(json);
		for (String key : keys) {
			Object value = json.opt(key);
			if(value!=null)
			  newJson.put(value.toString(), key);
		}
		return newJson;
	}
    
    public static JSONObject mergeJSON(JSONObject... objects) throws JSONException {
        JSONObject mergeJson = objects[0];
        for (int i = 1; i < objects.length; ++i) {
            JSONObject json = objects[i];
            if (json != null && json.length() > 0) {
                String[] keys = JSONObject.getNames(json);
                for (String key : keys) {
                    mergeJson.put(key, json.get(key));
                }
            }
        }
        return mergeJson;
    }

	public static JSONArray getJSONArray(String json) {
		if (json.startsWith("[")) {
			return new JSONArray(json.toString());
		}
		return new JSONArray().put(json);
	}
    
}

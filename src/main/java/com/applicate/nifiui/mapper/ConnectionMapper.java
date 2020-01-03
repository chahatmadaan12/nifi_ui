package com.applicate.nifiui.mapper;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.applicate.nifiui.configuration.provider.GlobalConfigurationProvider;
import com.applicate.utils.JSONUtils;

@Component
public class ConnectionMapper {

	@Autowired
	private GlobalConfigurationProvider configurationProvider;

	private final String MAPPED = "_mapped", UN_MAPPED = "_unMapped";

	private HashMap<String, HashMap<String, JSONObject>> conMapperHolder = new HashMap<String, HashMap<String, JSONObject>>();
	
	public JSONObject getUnMapped(String lob, String type) {
		return get(lob, type, UN_MAPPED);
	}
	
	public JSONObject getMapped(String lob, String type) {
		return get(lob, type, MAPPED);
	}

	private JSONObject get(String lob, String type, String key) {
		HashMap<String, JSONObject> connectionMapper = conMapperHolder.get(lob);
		if (connectionMapper != null) {
			JSONObject json = connectionMapper.get(type + key);
			return json != null ? json : new JSONObject();
		}
		put(lob);
		return get(lob, type, key);
	}

	private void put(String lob) {
		HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
		JSONObject connectionParam = configurationProvider.get(lob).getConnectionParam();
		String[] types = JSONObject.getNames(connectionParam);
		for (String type : types) {
			map.put(type + UN_MAPPED, connectionParam.getJSONObject(type));
			map.put(type + MAPPED,JSONUtils.getKeyAsValue(connectionParam.getJSONObject(type)));
		}
		conMapperHolder.put(lob, map);
	}

}

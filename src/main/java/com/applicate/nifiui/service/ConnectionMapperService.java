package com.applicate.nifiui.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.applicate.nifiui.mapper.ConnectionMapper;

@Service
public class ConnectionMapperService {
	
	@Autowired
	private ConnectionMapper connectionMapper ;
	
	/**
	 * this method will give data in database parameter
	 * @param data data will hold request data
	 * @param lob will hold client name
	 * @param type will contain connection type
	 * @return
	 */
	public JSONObject getWrappedConnection(JSONObject data,String lob,String type){
		JSONObject mapped = connectionMapper.getMapped(lob, type),json = new JSONObject();
		String[] names = JSONObject.getNames(data);
		for (String name : names) {
			json.put(mapped.optString(name,name), data.optString(name));
		}
		return json;
	}
	
	/**
	 * this method will give data in actual parameter
	 * @param data data will hold request data
	 * @param lob will hold client name
	 * @param type will contain connection type
	 * @return
	 */
	public JSONObject getUnWrappedConnection(JSONObject data,String lob,String type){
		JSONObject unMapped = connectionMapper.getUnMapped(lob, type),json = new JSONObject();
		String[] names = JSONObject.getNames(data);
		for (String name : names) {
			json.put(unMapped.optString(name,name), data.optString(name));
		}
		return json;
	}

}

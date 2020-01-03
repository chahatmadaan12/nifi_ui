package com.applicate.nifiui.configuration.provider;

import org.json.JSONObject;

import com.applicate.utils.JSONUtils;
import com.applicate.utils.StringUtils;

public abstract class AbstractConfigurationProvider implements ConfigurationProvider {

	protected String lob;

    AbstractConfigurationProvider() {
	}
	
	AbstractConfigurationProvider(String lob) {
		if (!StringUtils.isValidString(lob)) {
			throw new IllegalArgumentException("Given lob " + lob + " is not valid");
		}
		this.lob = lob;
	}

	protected abstract <T> T getGlobalHolder(String holderName, Class<T> clazz);

	protected abstract <T> T getClientHolder(String holderName, Class<T> clazz);

	protected JSONObject getJsonFromClientOrGlobal(String holderName, String key) {
		try {
			JSONObject clientHolder = getClientJsonHolder(holderName);
			if (JSONUtils.containsData(clientHolder) && clientHolder.has(key)) {
				return JSONUtils.safeClone(clientHolder.getJSONObject(key));
			}
			return JSONUtils.safeClone(getGlobalJsonHolder(holderName).optJSONObject(key));
		} catch (Exception e) {
			//LOGGER.error("Exception happened while getting configuration for " + holderName + " with key " + key);
		}
		return null;
	}

	protected JSONObject getClientOrGlobalJsonHolder(String holderName) {
		return JSONUtils
				.safeClone(JSONUtils.containsData(getClientJsonHolder(holderName)) ? getClientJsonHolder(holderName)
						: getGlobalJsonHolder(holderName));
	}

	protected JSONObject getClientJsonHolder(String holderName) {
		return getClientHolder(holderName, JSONObject.class);
	}

	protected JSONObject getGlobalJsonHolder(String holderName) {
		return getGlobalHolder(holderName, JSONObject.class);
	}

	@Override
	public JSONObject getConnectionParam(String type) {
		return getJsonFromClientOrGlobal(Configuration.YAML.CONNECTION_PARAM, type);
	}
	
	@Override
	public JSONObject getConnectionParam() {
		return getClientOrGlobalJsonHolder(Configuration.YAML.CONNECTION_PARAM);
	}

}

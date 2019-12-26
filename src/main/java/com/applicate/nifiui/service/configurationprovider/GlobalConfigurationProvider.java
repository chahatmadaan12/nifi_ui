package com.applicate.nifiui.service.configurationprovider;

import java.util.HashMap;

import com.applicate.nifiui.service.configurationprovider.configurationloader.ConfigurationLoaderService;
import com.applicate.nifiui.service.configurationprovider.configurationloader.impl.DefaultConfigurationLoader;

public class GlobalConfigurationProvider{

	private HashMap<String,ConfigurationLoaderService> configuratinHolder=new HashMap<String, ConfigurationLoaderService>();

	public boolean initConfigurationHolder(String lob) {
		try {
			configuratinHolder.put(lob, new DefaultConfigurationLoader());
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	public boolean reloadConfigurationHolder(String lob) {
		try {
			configuratinHolder.put(lob, new DefaultConfigurationLoader());
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	public boolean loadConfigurationHolder(String lob) {
		try {
			configuratinHolder.put(lob, new DefaultConfigurationLoader());
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	public ConfigurationLoaderService getGlobalConfig(String lob) {
		if(!configuratinHolder.containsKey(lob)) {

			return configuratinHolder.get(lob);
		}
		return configuratinHolder.get(lob);
	}

}
package com.applicate.nifiui.configuration.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.applicate.nifiui.configuration.reader.ConfigurationReader;
import com.applicate.nifiui.configuration.reader.ConfigurationReaderHelper;

public class DefaultConfigurationProvider extends AbstractConfigurationProvider implements ConfigurationProvider {
	
	private static ConfigurationReaderHelper helper = new ConfigurationReaderHelper();
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultConfigurationProvider.class);

	private static Map<String, Configuration> globalConfigurations = new ConcurrentHashMap<>();

	private Map<String, Configuration> clientConfigurations;

	private String folderName;

	public DefaultConfigurationProvider(String clientName) {
		super(clientName);
		this.clientConfigurations = new ConcurrentHashMap<>();
		this.folderName = clientName;
        load();
	}
    
	private void load() {
		loadGlobalConfigurations();
		loadClientConfigurations();
	}

	public void loadGlobalConfigurations() {
		loadDefaultNonCachableConfigurations();
	}

	public void loadClientConfigurations() {
		loadClientNonCachableConfigurations();
	}

	private void loadClientNonCachableConfigurations() {
		loadConfigurations(clientConfigurations, false, getFolderName());
	}

	private void loadDefaultNonCachableConfigurations() {
		loadConfigurations(globalConfigurations, true, getDefaultClientName());
	}

	public String getFolderName() {
		return folderName;
	}

	public String getDefaultClientName() {
		return "applicate";
	}

	private void loadConfigurations(Map<String, Configuration> configurationHolder, boolean isGlobal,String folderName) {
		List<ConfigurationReader> readers = getReaders(isGlobal);
		for (ConfigurationReader reader : readers) {
			configurationHolder.put(reader.getTitle(), new DefaultConfiguration(reader, folderName));
		}
	}

	private List<ConfigurationReader> getReaders(boolean isGlobal) {
		ConfigurationReader[] readers = getConfigurationReaders();
		List<ConfigurationReader> filteredReaders = new ArrayList<>();
		for (ConfigurationReader reader : readers) {
			filterConfigurationReaderAndPut(filteredReaders, reader, isGlobal);
		}
		return filteredReaders;
	}

	private void filterConfigurationReaderAndPut(List<ConfigurationReader> readers, ConfigurationReader reader,boolean isGlobal) {
		if (isGlobal && reader.isGlobalConfiguration()) {
			reader.setAsGlobalConfiguration();
			readers.add(reader);
		}
		if (!isGlobal && reader.isClientConfiguration()) {
			reader.setAsClientConfiguration();
			readers.add(reader);
		}
	}

	private ConfigurationReader[] getConfigurationReaders() {
		ConfigurationReader[] reader = {
				helper.createCommonJsonReader(Configuration.YAML.CONNECTION_PARAM,"configs/{{clientName}}/connectionParamMapping.yaml"),
				helper.createCommonJsonReader(Configuration.YAML.CONSTANTS,"configs/{{clientName}}/constants.yaml") 
				
		};
		return reader;
	}

	private <T> T getHolder(Map<String, Configuration> configurationMap, String holderName, Class<T> clazz) {
		try {
			Configuration holder = configurationMap.get(holderName);
			if (holder != null) {
				return clazz.cast(holder.getConfiguration());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected <T> T getClientHolder(String holderName, Class<T> clazz) {
		return getHolder(clientConfigurations, holderName, clazz);
	}

	@Override
	protected <T> T getGlobalHolder(String holderName, Class<T> clazz) {
		return getHolder(globalConfigurations, holderName, clazz);
	}

}

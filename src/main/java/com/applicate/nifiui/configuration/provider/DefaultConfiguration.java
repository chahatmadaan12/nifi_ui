package com.applicate.nifiui.configuration.provider;

import java.util.HashMap;

import com.applicate.nifiui.configuration.reader.ConfigurationReader;


public class DefaultConfiguration<T> implements Configuration<T> {

	private ConfigurationReader<T> configurationReader;

	private String folderName;

	private HashMap<String, T> configHolder = new HashMap<String, T>();
	
	public DefaultConfiguration(ConfigurationReader reader, String folderName) {
		this.configurationReader = reader;
		this.folderName = folderName;
		readValue();
	}

	@Override
	public T getConfiguration() {
		T value = this.configHolder.getOrDefault(configurationReader.getTitle(), null);
		if (value == null) {
			value = configurationReader.read(folderName);
			setConfiguration(value);
		}
		return value;
	}

	@Override
	public void reLoad() {

	}

	@Override
	public void setConfiguration(T value) {
		this.configHolder.put(configurationReader.getTitle(), value);
	}

	private void readValue() {
		setConfiguration(configurationReader.read(this.folderName));
	}

}

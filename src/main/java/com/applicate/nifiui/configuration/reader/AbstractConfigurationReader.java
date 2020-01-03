package com.applicate.nifiui.configuration.reader;

import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONObject;

public abstract class AbstractConfigurationReader<T> implements ConfigurationReader<T> {

	public static final Class[] SUPPORTED_CLASSES = { JSONObject.class, Properties.class, String.class };

	private String filePath, title;

	private Class clazz;

    private boolean isGlobalConfiguration;

    private boolean isClientConfiguration;
    
	public AbstractConfigurationReader(String title, Class clazz, String filePath,boolean isGlobal,boolean isClient) {
		validateClass(clazz);
		this.title = title;
		this.filePath = filePath;
		this.clazz = clazz;
        this.isGlobalConfiguration = isGlobal;
        this.isClientConfiguration = isClient;
	}

	@Override
	public Class getType() {
		return this.clazz;
	}

	public String getFilePath() {
		return this.filePath;
	}
    
	@Override
	public String getTitle() {
		return title;
	}

	protected void validateClass(Class clazz) {
		if (!ArrayUtils.contains(SUPPORTED_CLASSES, clazz)) {
			throw new IllegalArgumentException("Given class " + clazz + " is not supported for Configuration reading");
		}
	}
	
	@Override
    public boolean isGlobalConfiguration() {
        return isGlobalConfiguration;
    }

    @Override
    public boolean isClientConfiguration() {
        return isClientConfiguration;
    }
    
    @Override
    public ConfigurationReader<T> setAsGlobalConfiguration() {
        this.isGlobalConfiguration = true;
        this.isClientConfiguration = false;
        return this;
    }

    @Override
    public ConfigurationReader<T> setAsClientConfiguration() {
        this.isGlobalConfiguration = false;
        this.isClientConfiguration = true;
        return this;
    }
    
}

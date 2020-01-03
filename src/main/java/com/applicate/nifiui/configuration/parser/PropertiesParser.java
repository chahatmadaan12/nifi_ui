package com.applicate.nifiui.configuration.parser;

import java.io.File;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import com.applicate.utils.StringUtils;

public class PropertiesParser<T> extends AbstractParser<T> {

	private Properties properties;

	private PropertiesConfigurationLayout layout;

	private PropertiesConfiguration configuration;

	public PropertiesParser(String source) {
		this(source != null ? new File(source) : null);
	}

	public PropertiesParser(File sourceFile) {
		setSource(sourceFile);
	}

	private void removeUnwantedKeysFromConfiguration(Properties props) {
		Enumeration keys = props.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			configuration.clearProperty(key);
		}
	}

	@Override
	public T get() {
		return (T) cloneProperties(this.properties);
	}

	@Override
	protected void convertToSpecificFormat(String content) throws Exception   {
		this.configuration = new PropertiesConfiguration();
		this.configuration.setDelimiterParsingDisabled(true);
		this.layout = new PropertiesConfigurationLayout(configuration);
		this.properties = new Properties();
		if (!content.isEmpty()) {
			try {
				layout.load(new StringReader(content));
				setProperties(this.properties, configuration);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
	}

	private void setProperties(Properties props, PropertiesConfiguration configuration) {
		Iterator<String> keys = configuration.getKeys();
		while (keys.hasNext()) {
			String currentKey = keys.next();
			String currentValue = configuration.getString(currentKey);
			props.setProperty(currentKey, StringUtils.isValidString(currentValue) ? currentValue.trim() : currentValue);
		}
	}

	@Override
	protected String getContent() {
		throw new RuntimeException("Operation is not allowed");
	}
	
	public static Properties cloneProperties(Properties source) {
        Properties properties = new Properties();
        for (Entry<Object, Object> entry: source.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            properties.put(key, value);
        }
        return properties;
    }

}

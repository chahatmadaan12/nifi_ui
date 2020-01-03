package com.applicate.nifiui.configuration.reader;


public class ConfigurationReaderFactory {
		
	public static <T> ConfigurationReader<T> createConfigurationReader(String title, Class configurationTypeClass,String filePath, boolean isGlobal, boolean isClient) {
		return new FileConfigurationReader(title,configurationTypeClass, filePath,isGlobal,isClient); 
	}
}

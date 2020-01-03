package com.applicate.nifiui.configuration.reader;

public interface ConfigurationReader<T> {

	T read(String clientName);

	Class getType();

	String getTitle();

	boolean isGlobalConfiguration();

	boolean isClientConfiguration();

	ConfigurationReader<T> setAsGlobalConfiguration();

	ConfigurationReader<T> setAsClientConfiguration();

}

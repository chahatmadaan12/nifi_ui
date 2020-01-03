package com.applicate.nifiui.configuration.provider;

public interface Configuration<T> {

	interface YAML {
		String CONNECTION_PARAM = "connectionParamMappingYaml";
	}

	interface JSON {

	}

	interface PROPERTIES {

	}

	T getConfiguration();

	void reLoad();

	void setConfiguration(T item);
}

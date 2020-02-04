package com.applicate.nifiui.configuration.reader;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.applicate.nifiui.configuration.parser.ParserFactory;
import com.applicate.utils.FileUtils;
import com.applicate.utils.JSONUtils;

public class FileConfigurationReader<T> extends AbstractConfigurationReader<T> implements ConfigurationReader<T> {

	private Logger log = LoggerFactory.getLogger(FileConfigurationReader.class);

	public FileConfigurationReader(String title, Class clazz, String filePath,boolean isGlobal,boolean isClient) {
		super(title, clazz, filePath,isGlobal,isClient);
	}

	@Override
	public T read(String clientName) {
		File file = new File(FileUtils.getAbsoluteConfigurationPath(getFilePath(),"clientName", clientName));
		T data = readFromFile(file);
		return data;
	}

	@SuppressWarnings("unchecked")
	protected T readFromFile(File file) {
		try {
			return (T) ParserFactory.getParser(file).parse().get();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return (T) generateEmptyConfiguration(getType(), file);
	}

	protected Object generateEmptyConfiguration(Class clazz, File file) {
		if (clazz.equals(JSONObject.class)) {
			return JSONUtils.getEmptyObject();
		}
		if (clazz.equals(Properties.class)) {
			return new Properties();
		}
		if (clazz.equals(Map.class)) {
			return new LinkedHashMap<>();
		}
		if (clazz.equals(String.class)) {
			return "";
		}
		return null;
	}
}

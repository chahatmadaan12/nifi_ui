package com.applicate.nifiui.configuration.parser;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.applicate.utils.FileUtils;

public class JsonParser<T extends JSONObject> extends AbstractParser<T> {

	private JSONObject data;

	public JsonParser(String sourceFilePath) {
		this(sourceFilePath != null ? new File(sourceFilePath) : null);
	}

	public JsonParser(File sourceFile) {
		setSource(sourceFile);
	}

	@Override
	public T get() {
		try {
			return (T) new JSONObject(this.data.toString());
		} catch (JSONException e) {
			return (T) new JSONObject();
		}
	}

	@Override
	protected void convertToSpecificFormat(String content) throws Exception {
		this.data = convertToJSONObject(content);
	}

	private JSONObject convertToJSONObject(String content) throws Exception {
		try {
			if (FileUtils.isXmlFile(getSource())) {
				return XML.toJSONObject(content);
			}
			return content.isEmpty() ? new JSONObject() : new JSONObject(content);
		} catch (JSONException e) {
			throw new Exception("Exception happend while parsing " + getSource() + " , Reason : " + e.getMessage());
		}
	}

	@Override
	protected String getContent() throws IOException {
		try {
			return this.data.toString(4);
		} catch (JSONException e) {
			return this.data.toString();
		}
	}

}

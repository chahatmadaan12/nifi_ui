package com.applicate.nifiui.configuration.parser;

import java.io.File;

import org.json.JSONObject;

import com.applicate.utils.JSONUtils;
import com.applicate.utils.StringUtils;

public class YamlParser<T> extends AbstractParser<JSONObject> {

	private JSONObject content;

	public YamlParser(String sourceFileName) {
		this(sourceFileName != null ? new File(sourceFileName) : null);
	}

	public YamlParser(File source) {
		setSource(source);
	}

	@Override
	public JSONObject get() {
		return JSONUtils.cloneJSONObject(content);
	}

	@Override
	protected void convertToSpecificFormat(String content) throws Exception {
		if (StringUtils.isEmpty(content)) {
			this.content = new JSONObject();
			return;
		}
		try {
			this.content = new JSONObject(JSONUtils.yamlToJson(content));
		} catch (Exception e) {
			throw new Exception("Exception happened while converting yaml String to json for file " + getSource(),
					e);
		}
	}

	@Override
	protected String getContent() throws Exception {
		try {
			return JSONUtils.jsonToYaml(content.toString());
		} catch (Exception e) {
			throw new Exception("Could not convert yaml ot json", e);
		}
	}
}

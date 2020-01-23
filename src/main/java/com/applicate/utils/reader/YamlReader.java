package com.applicate.utils.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
@Component
public class YamlReader implements Reader<JSONObject>{
	
	private Yaml yaml = new Yaml();

	@Override
	public JSONObject get(String path) throws FileNotFoundException {
		Map<String,Object> map= (Map<String, Object>)yaml.load(new FileReader(new File(path)));
		return new JSONObject(map);
	}

}

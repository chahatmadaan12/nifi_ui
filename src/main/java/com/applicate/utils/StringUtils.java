package com.applicate.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class StringUtils {
  
	public static String replaceDynamicValues(String str,JSONObject dataObj) throws JSONException{
		Pattern pattern = Pattern.compile("\\{\\{[^}]*}}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String matcherStr = matcher.group();
			String key = matcherStr.replaceAll(Pattern.quote("{{"), "");
			key = key.replaceAll(Pattern.quote("}}"), "");
			String value = "";
			String[] keyArr = key.split(":");
			for (String keystr : keyArr) {
				value = dataObj.optString(keystr);
				if(!keystr.contains("'") && "".equals(value)){
					continue;
				}else{
					if(!"".equals(value)){
						break;
					}
					if(keystr.contains("'")){
						value = keystr.replaceAll(Pattern.quote("'"), "");
						break;
					}
				}
			}
			str = str.replaceAll(Pattern.quote(matcherStr), value);
		}
		return str;
	}
	
}

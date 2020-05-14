package com.applicate.utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlHepler {
	
	public static final String REGEX = "[\"'<>]";
	
	public static HashMap<String, String> specialCharMap = new HashMap<>();
	
	static {
		specialCharMap.put("&", "&amp;");
		specialCharMap.put("'", "&apos;");
		specialCharMap.put("\"", "&quot;");
		specialCharMap.put("<", "&lt;");
		specialCharMap.put(">", "&gt;");
	}
    
	public static String replaceSpecialCharacter(String target) {
		target = target.replace("&", specialCharMap.get("&"));
	    Pattern compile = Pattern.compile(REGEX);
	    Matcher matcher = compile.matcher(target);
	    while(matcher.find()) {
	    	String arg = matcher.group();
	    	target = target.replace(arg,specialCharMap.get(arg));
	    }
		return target;
	}
	
	public static void main(String[] args) {
		 System.out.println("abc&xml\'\'&al<f>\\     "+XmlHepler.replaceSpecialCharacter("abc&xml''&al<f>"));
	}
	
}

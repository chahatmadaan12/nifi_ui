package com.applicate.nifiui.configuration.parser;

public interface Parser<T> {

	Parser<T> parse() throws Exception;

	T get();
	
}

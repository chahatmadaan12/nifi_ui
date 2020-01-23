package com.applicate.utils.reader;

import java.io.FileNotFoundException;

import org.springframework.stereotype.Component;

@Component
public interface Reader<T> {
	
	T get(String path) throws FileNotFoundException;

}

package com.applicate.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class CollectionUtils {

	public static <K, V> Map<V, K> interChangeKeyValue(Map<K, V> map) {
		Map<V, K> newMap = new LinkedHashMap<>();
		for (K key : map.keySet()) {
			V value = map.get(key);
			newMap.put(value, key);
		}
		return newMap;
	}
}

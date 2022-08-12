package com.watermelon.core.di.modules;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MapConfiguration<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {
	private static final long serialVersionUID = 6083656736390303684L;
	private static final String NULL_REFERENCE = "Key cannot be null";

	public String getString(String key) {
		Objects.requireNonNull(key, NULL_REFERENCE);
		return (String) get(key);
	}

	@SuppressWarnings("unchecked")
	public Map<K, V> getMap(String key) {
		Objects.requireNonNull(key, NULL_REFERENCE);
		return (Map<K, V>) get(key);
	}

	public Integer getInt(String key) {
		Objects.requireNonNull(key, NULL_REFERENCE);
		return (Integer) get(key);
	}

	public Double getDouble(String key) {
		Objects.requireNonNull(key, NULL_REFERENCE);
		return (Double) get(key);
	}
}

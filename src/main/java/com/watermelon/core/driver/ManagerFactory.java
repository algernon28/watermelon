package com.watermelon.core.driver;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.watermelon.core.UnsupportedBrowserException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ManagerFactory {

	@SuppressWarnings("unchecked")
	public static <T extends WebDriver> Manager<T> newManager(T type, final Map<String, String> parameters)
			throws UnsupportedBrowserException {
		Manager<T> result;
		try {
			result = (Manager<T>) type.getClass().getConstructor(Map.class).newInstance(parameters);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new UnsupportedBrowserException("Could not create Manager", e);
		}
		return result;
	}
}

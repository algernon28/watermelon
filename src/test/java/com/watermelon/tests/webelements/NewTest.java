package com.watermelon.tests.webelements;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.watermelon.core.driver.ChromeManager;
import com.watermelon.core.driver.Manager;

public class NewTest {
	@Test
	public void f() throws Throwable {
		ChromeManager cm;
		ParameterizedType myInterface = (ParameterizedType) ChromeManager.class.getGenericInterfaces()[0];
		String name = myInterface.getActualTypeArguments()[0].getTypeName();
		ParameterizedType genericClass = getGenericClassName(ChromeManager.class,
				com.watermelon.core.driver.Manager.class);
		List<Type> paramTypes = Arrays.asList(genericClass.getActualTypeArguments());
		Assertions.assertThat(isInstanceOf(ChromeManager.class, Manager.class, ChromeDriver.class)).isTrue();
	}

	public static boolean isInstanceOf(Class<?> clazz, Class<?> interfaceName, Class<?> genericParam) {
		ParameterizedType parameterizedClass = getGenericClassName(ChromeManager.class,
				com.watermelon.core.driver.Manager.class);
		return Arrays.asList(parameterizedClass.getActualTypeArguments()).contains(genericParam);

	}

	public static ParameterizedType getGenericClassName(Class clazz, Class<?> interfaceName) {
		Type[] interfaces = ChromeManager.class.getGenericInterfaces();
		Optional<Type> type = Arrays.asList(interfaces).stream()
				.filter(i -> ((ParameterizedType) i).getRawType().equals(com.watermelon.core.driver.Manager.class))
				.findAny();
		ParameterizedType result = null;
		if (type.isPresent()) {
			result = (ParameterizedType) type.get();
		}
		return result;
	}
}

package com.watermelon.core;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.testng.xml.XmlTest;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import com.watermelon.core.di.modules.Configuration;
import com.watermelon.core.di.modules.MapConfiguration;

import io.github.bonigarcia.wdm.config.Architecture;
import io.github.bonigarcia.wdm.config.OperatingSystem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
	public static final String DEFAULT_LANG = "en";
	public static final Architecture DEFAULT_ARCH = Architecture.X64;
	public static final OperatingSystem DEFAULT_OS = OperatingSystem.WIN;
	public static final String DEFAULT_COUNTRY = "US";
	public static final String DEFAULT_BROWSER = "Chrome";
	public static final String DEFAULT_VERSION = "latest";

	private Utils() {

	}

	/**
	 * Deserialize the yaml configuration file into an instance of T type
	 *
	 * @param <T>      the type representing the configuration data (ex:
	 *                 {@linkplain Configuration})
	 * @param clazz    the class object holding the type
	 * @param fileName the name of the yaml configuration file. It is resolved from
	 *                 the classpath.
	 * @return the populated configuration object
	 */
	public static <T> T loadYaml(Class<T> clazz, String fileName) {
		Constructor constructor = new Constructor(clazz);
		PropertyUtils props = new PropertyUtils();
		props.setSkipMissingProperties(true);
		Representer rep = new Representer();
		rep.setPropertyUtils(props);
		Yaml yamlConfig = new Yaml(constructor, rep);
		InputStream is = clazz.getClassLoader().getResourceAsStream(fileName);
		return yamlConfig.load(is);
	}

	/**
	 *
	 * @param <V>
	 * @param <K>
	 * @param fileName
	 * @return
	 */
	public static <V, K> MapConfiguration<K, V> fromYaml(String fileName) {
		Yaml yaml = new Yaml();
		MapConfiguration<K, V> result = new MapConfiguration<>();
		InputStream is = Utils.class.getClassLoader().getResourceAsStream(fileName);
		Map<K, V> map = yaml.load(is);
		result.putAll(map);
		return result;
	}

	/**
	 * Lookup a parameter from a TestNG suite context
	 *
	 * @param key     the name of the parameter
	 * @param context the {@link XmlTest} context from the TestNG suite file
	 * @return the value of the parameter
	 */
	public static Optional<String> lookupParameter(String key, XmlTest context) {
		String param = context.getParameter(key);
		Optional<String> result = Optional.ofNullable(param);
		log.debug("Returning property: {}", result);
		return result;
	}

	/**
	 *
	 * @param key the name of the property
	 * @return the value of the System property
	 */
	public static Optional<String> lookupProperty(String key) {
		Optional<String> result = Optional.ofNullable(System.getProperty(key));
		log.debug("Returning property: {}", result);
		return result;
	}

	/**
	 *
	 * @param csv a comma separated values String
	 * @return the corresponding List of strings
	 */
	public static List<String> csvToStringList(String csv) {
		return List.of(csv.split(","));
	}

	/**
	 *
	 * @param csv a comma separated values String
	 * @return the corresponding List of integers
	 */
	public static List<Integer> csvToIntList(String csv) {
		return List.of(csv.split(",")).stream().map(val -> {
			Integer result;
			try {
				result = Integer.parseInt(val);
			} catch (NumberFormatException e) {
				result = null;
			}
			return result;
		}).filter(Objects::nonNull).toList();
	}

	/**
	 *
	 * @param csv a comma separated values String
	 * @return the corresponding List of doubles
	 */
	public static List<Double> csvToDoubleList(String csv) {
		return List.of(csv.split(",")).stream().map(val -> {
			Double result;
			try {
				result = Double.parseDouble(val);
			} catch (NumberFormatException e) {
				result = null;
			}
			return result;
		}).filter(Objects::nonNull).toList();
	}

	/**
	 * Given a class and an interface it implements, and a generic type, this method
	 * verifies that the class implements the interface of that type. For instance,
	 * given a class like
	 * <i>{@code Mickey implements Character<Mouse>, someotherinterfaces}</i>, an
	 * invocation like {@link #isInstanceOf(Mickey.class, Character.class,
	 * Mouse.class)}, the method will return true.
	 * 
	 * @param myClass       the class to be queried
	 * @param interfaceName the implemented interface to look for
	 * @param genericParam  the generic type to be verified
	 * @return {@code true} if the class implements interfaceName<genericParam>,
	 *         {@code false} otherwise.
	 */
	public static boolean isInstanceOf(Class<?> myClass, Class<?> interfaceName, Class<?> genericParam) {
		ParameterizedType parameterizedClass = getGenericClassType(myClass, interfaceName);
		Objects.requireNonNull(parameterizedClass);
		return Arrays.asList(parameterizedClass.getActualTypeArguments()).contains(genericParam);

	}

	/**
	 * Given a class and an interface it implements, this method returns the
	 * {@link ParameterizedType} of the interface. For instance, given a class like
	 * <i>{@code Mickey implements Character<Mouse>, someotherinterfaces}</i>, it
	 * will return a Type whose rawType is {@code Character} and an array of actual
	 * Type arguments (in this case [Mouse])
	 * 
	 * @param myClass     the class to be queried
	 * @param myInterface the implemented interface to look for
	 * @return the generic Type
	 */
	public static ParameterizedType getGenericClassType(Class<?> myClass, Class<?> myInterface) {
		Type[] interfaces = myClass.getGenericInterfaces();
		Optional<Type> type = Arrays.asList(interfaces).stream()
				.filter(i -> ((ParameterizedType) i).getRawType().equals(myInterface)).findAny();
		ParameterizedType result = null;
		if (type.isPresent()) {
			result = (ParameterizedType) type.get();
		}
		return result;
	}
}

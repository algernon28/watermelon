package com.watermelon.core.di.modules;

import static com.watermelon.core.Utils.DEFAULT_COUNTRY;
import static com.watermelon.core.Utils.DEFAULT_LANG;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.watermelon.core.Utils;

import io.cucumber.guice.ScenarioScoped;

/**
 * Make the following objects available for Dependency Injection:
 * {@link Configuration}, {@link Locale}, {@link ResourceBundle}
 *
 * @author AM
 *
 */

public class ConfigurationModule extends AbstractModule {
	private static final Logger log = LoggerFactory.getLogger(ConfigurationModule.class);
	private static final String CONFIGFILE_PARAM = "stage";

	@Override
	protected void configure() {
		log.debug("Configuring {}", getClass().getSimpleName());
	}

	@Provides
	public Configuration getConfiguration() {
		String stage = Optional.ofNullable(System.getenv(CONFIGFILE_PARAM))
				.orElse(System.getProperty(CONFIGFILE_PARAM));
		Configuration config = Utils.loadYaml(Configuration.class, stage);
		config.setGithubToken(System.getProperty("githubToken"));
		log.debug("Configuration loaded: {}", config);
		return config;
	}

	@Provides
	@ScenarioScoped
	public Locale getLocale() {
		Locale.setDefault(Locale.ENGLISH);
		String language = Utils.lookupProperty("language").orElse(DEFAULT_LANG);
		String country = Utils.lookupProperty("country").orElse(DEFAULT_COUNTRY);
		return new Locale(language, country);
	}

	@Provides
	@ScenarioScoped
	public ResourceBundle getBundle(Locale locale) {
		return ResourceBundle.getBundle("messages", locale);
	}

}

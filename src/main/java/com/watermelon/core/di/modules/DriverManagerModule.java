package com.watermelon.core.di.modules;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Make the following objects available for Dependency Injection:
 * {@link DriverManager}, {@link JavascriptExecutor}, {@link WebDriverWait}, {@link Actions}
 *
 * @author AM
 *
 */
public class DriverManagerModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(DriverManagerModule.class);

	public static final Duration DefaultTimeout = Duration.ofSeconds(30);
	public static final Duration DefaultTimeoutInterval = Duration.ofMillis(500);

	@Override
	protected void configure() {
		logger.debug("Configuring {}", getClass().getSimpleName());
		bind(RemoteWebDriver.class).toProvider(DriverManager.class);
	}

	@Provides
	public JavascriptExecutor getJavascriptExecutor(RemoteWebDriver driver) {
		return driver;
	}

	@Provides
	public Actions getActions(RemoteWebDriver driver) {
		return new Actions(driver);
	}

	@Provides
	public WebDriverWait getWait(RemoteWebDriver driver) {
		return new WebDriverWait(driver, DefaultTimeout, DefaultTimeoutInterval);
	}
}

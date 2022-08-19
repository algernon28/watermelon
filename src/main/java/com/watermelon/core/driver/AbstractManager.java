package com.watermelon.core.driver;

import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.WebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;

public abstract class AbstractManager<T extends WebDriver> implements Manager<T> {
	protected Optional<String> myVersion;
	protected Optional<String> myLanguage;
	protected Optional<String> myCountry;
	protected Optional<String> myOS;
	protected String myBrowser;
	protected DriverManagerType browser;

	protected WebDriverManager wdm;
	protected WebDriver driver;

	protected AbstractManager(Map<String, String> parameters) {
		myVersion = Optional.ofNullable(parameters.get("version"));
		myLanguage = Optional.ofNullable(parameters.get("language"));
		myCountry = Optional.ofNullable(parameters.get("country"));
		myOS = Optional.ofNullable(parameters.get("os"));
		myBrowser = parameters.get("browser");
		browser = DriverManagerType.valueOf(myBrowser.toUpperCase());
	}

}

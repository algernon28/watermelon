package com.watermelon.steps;

import java.util.ResourceBundle;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.core.di.modules.MapConfiguration;

public abstract class BaseSteps {

	public static final String BASE_MESSAGENAME = "messages";

	@Inject
	protected WebDriver driver;

	@Inject
	protected JavascriptExecutor jsExecutor;

	@Inject
	protected WebDriverWait waiter;

	@Inject
	protected Actions actions;


	@Inject
	private MapConfiguration<String, Object> config;

	@Inject
	protected ResourceBundle bundle;

	protected BaseSteps() {
	}
}

package com.watermelon.core;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.core.di.modules.DriverManagerModule;
import com.watermelon.core.di.modules.MapConfiguration;

/**
 * Page Object Parent class
 *
 * @author AM
 *
 */
public abstract class WebPage extends Page{


	protected WebPage(WebDriver driver, WebDriverWait wait) {
		this.driver = (RemoteWebDriver) driver;
		this.wait = wait;
		PageFactory.initElements(driver, this);
	}

	/**
	 *
	 * @return the element identifying the page
	 */
	public abstract WebElement getTitle();

}

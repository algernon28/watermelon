package com.watermelon.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

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

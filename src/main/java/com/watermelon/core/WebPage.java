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
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.core.di.modules.Configuration;
import com.watermelon.core.di.modules.DriverManagerModule;

/**
 * Page Object Parent class
 * 
 * @author AM
 *
 */
public abstract class WebPage {

	protected WebDriver driver;

	protected WebDriverWait wait;

	@Inject
	protected JavascriptExecutor jsExecutor;

	@Inject
	protected Configuration configuration;

	protected WebPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		PageFactory.initElements(driver, this);
	}

	/**
	 *
	 * @return the element identifying the page
	 */
	public abstract WebElement getTitle();

	/**
	 *
	 * @return if the page was loaded. The check is made on the page title. Every
	 *         Page Object must implement it.
	 */
	public boolean isLoaded() {
		WebElement pageTitle = getTitle();
		waitUntilVisible(pageTitle);
		return pageTitle.isDisplayed();
	}

	public void verify() {
		if (!this.isLoaded()) {
			String msg = String.format("Could not load page %s", getClass().getSimpleName());
			throw new PageNotLoadedException(msg);
		}
	}

	/**
	 * Wait {@link DriverManagerModule#DefaultTimeout} or until the element is
	 * visible. Will throw an unchecked {@link TimeoutException} if the element is
	 * not visible by the timeout.
	 * 
	 * @param element to be checked
	 * @return the element itself
	 */
	public WebElement waitUntilVisible(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Click an element if visible
	 * 
	 * @see #waitUntilVisible(WebElement)
	 * @param element the element to be clicked
	 */
	protected void click(WebElement element) {
		waitUntilVisible(element).click();
	}

	/**
	 * Clear a text element if visible. It only works for text and password
	 * elements.
	 * 
	 * @see #waitUntilVisible(WebElement)
	 * @param element the element to be cleared
	 */
	protected void clear(WebElement element) {
		waitUntilVisible(element).clear();
	}

	/**
	 * Clear all the elements. It only works for text and password elements.
	 * 
	 * @see #clear(WebElement)
	 * @param elements the {@link List} of elements to be cleared
	 */
	protected void clearAll(List<WebElement> elements) {
		elements.stream().forEach(WebElement::clear);
	}

	/**
	 * @see #clearAll(List)
	 * @param elements an array of elements to be cleared
	 */
	protected void clearAll(WebElement... elements) {
		clearAll(Arrays.asList(elements));
	}

	/**
	 * Clear the element and type in the text value. It only works for text and
	 * password elements.
	 * 
	 * @param element the element where to type the text
	 * @param value   the text to be typed in
	 */
	protected void type(WebElement element, String value) {
		clear(element);
		waitUntilVisible(element).sendKeys(value);
	}

	/**
	 * Append text into an element
	 * 
	 * @see #type(WebElement, String)
	 * @param element the element where to append the text
	 * @param value   the text to be appended
	 */
	protected void addText(WebElement element, String value) {
		waitUntilVisible(element).sendKeys(value);
	}

	public Optional<WebElement> isPresent(By elementLocator) {
		try {
			return Optional.of(driver.findElement(elementLocator));
		} catch (NoSuchElementException e) {
			return Optional.empty();
		}
	}
}

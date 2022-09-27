package com.saucedemo.web.pages.checkout;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;

public class CheckoutInfo extends CheckoutPage {
	@FindBys({ @FindBy(xpath = "//span[@class='title']"),
			@FindBy(xpath = "//span[normalize-space()='Checkout: Your Information']") })
	@CacheLookup
	private WebElement title;

	@FindBy(id = "first-name")
	@CacheLookup
	private WebElement firstName;

	@FindBy(id = "last-name")
	@CacheLookup
	private WebElement lastName;

	@FindBy(id = "postal-code")
	@CacheLookup
	private WebElement postalCode;

	@Inject
	public CheckoutInfo(RemoteWebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	@Override
	public WebElement getTitle() {
		waitUntilVisible(title);
		return title;
	}

	public void fillInfo(String myFirstName, String myLastName, String myPostalCode) {
		firstName.sendKeys(myFirstName);
		lastName.sendKeys(myLastName);
		postalCode.sendKeys(myPostalCode);
	}


	@Override
	public boolean isLoaded() {
		WebElement pageTitle = getTitle();
		waitUntilVisible(pageTitle);
		return pageTitle.isDisplayed();
	}
}

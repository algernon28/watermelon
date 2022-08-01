package com.watermelon.pages.checkout;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.pages.SauceLabsPage;

public class CheckoutCompletePage extends SauceLabsPage {

	@FindBy(id = "back-to-products")
	@CacheLookup
	private WebElement btnBackHome;

	@FindBy(className = "complete-header")
	private WebElement title;

	@FindBy(className = "complete-text")
	private WebElement pageDescription;

	@Inject
	public CheckoutCompletePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public void backHome() {
		btnBackHome.click();
	}

	@Override
	public WebElement getTitle() {
		waitUntilVisible(title);
		return title;
	}

	@Override
	public boolean isLoaded() {
		WebElement pageTitle = getTitle();
		waitUntilVisible(pageTitle);
		return pageTitle.isDisplayed();
	}

}

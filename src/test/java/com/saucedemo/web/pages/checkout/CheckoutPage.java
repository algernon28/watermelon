package com.saucedemo.web.pages.checkout;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.saucedemo.web.pages.SauceLabsPage;

public abstract class CheckoutPage extends SauceLabsPage {

	@Inject
	protected CheckoutButtons checkoutButtons;

	public CheckoutPage(RemoteWebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public void continueCheckout() {
		checkoutButtons.continueCheckout();
	}

	public void cancelCheckout() {
		checkoutButtons.cancelCheckout();
	}
}

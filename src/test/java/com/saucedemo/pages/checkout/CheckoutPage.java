package com.saucedemo.pages.checkout;

import com.google.inject.Inject;
import com.saucedemo.pages.SauceLabsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class CheckoutPage extends SauceLabsPage {

	@Inject
	protected CheckoutButtons checkoutButtons;

	public CheckoutPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public void continueCheckout() {
		checkoutButtons.continueCheckout();
	}

	public void cancelCheckout() {
		checkoutButtons.cancelCheckout();
	}
}

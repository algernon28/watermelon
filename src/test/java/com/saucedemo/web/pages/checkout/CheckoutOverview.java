package com.saucedemo.web.pages.checkout;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.saucedemo.web.pages.SauceItem;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutOverview extends CheckoutPage {
	@FindBys({ @FindBy(xpath = "//span[@class='title']"),
			@FindBy(xpath = "//span[normalize-space()='Checkout: Overview']") })
	@CacheLookup
	private WebElement title;

	@FindBy(className = "cart_item")
	@CacheLookup
	private List<WebElement> cartItems;

	@FindBy(xpath = "//div[normalize-space()='Payment Information:']/following-sibling::div[@class='summary_value_label']")
	private WebElement paymentInfoValue;

	@FindBy(xpath = "//div[normalize-space()='Shipping Information:']/following-sibling::div[@class='summary_value_label']")
	private WebElement shippingInfoValue;
	@FindBy(className = "summary_subtotal_label")
	private WebElement summarySubTotalLabel;
	@FindBy(className = "summary_tax_label")
	private WebElement summaryTaxLabel;
	@FindBy(className = "summary_total_label")
	private WebElement summaryTotalLabel;

	@Inject
	public CheckoutOverview(RemoteWebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public List<SauceItem> getItems() {
		return cartItems.stream().map(el -> new SauceItem(el)).toList();
	}

	public String getPaymentInfo() {
		return paymentInfoValue.getText();
	}

	public String getShippingInfo() {
		return shippingInfoValue.getText();
	}

	public double getSubTotalValue() {
		return getMoneyValue(summarySubTotalLabel.getText());
	}

	public double getTaxValue() {
		return getMoneyValue(summaryTaxLabel.getText());
	}

	public double getTotalValue() {
		return getMoneyValue(summaryTotalLabel.getText());
	}

	@SneakyThrows
	private double getMoneyValue(String label) {
		String moneyString = label.split(": ")[1]; //Get the right-side part of "blabla: $999.00"
		double result = NumberFormat.getCurrencyInstance(Locale.US).parse(moneyString).doubleValue();
		log.debug("money = {}", result);
		return result;
	}

	public void finish() {
		checkoutButtons.finishCheckout();
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

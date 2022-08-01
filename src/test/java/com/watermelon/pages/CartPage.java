package com.watermelon.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;

public class CartPage extends SauceLabsPage {

	@Inject
	public CartPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	@FindBys({ @FindBy(xpath = "//span[@class='title']"), @FindBy(xpath = "//span[normalize-space()='Your Cart']") })
	@CacheLookup
	private WebElement title;

	@FindBy(className = "cart_item")
	@CacheLookup
	private List<WebElement> cartItems;

	@FindBy(id = "checkout")
	@CacheLookup
	private WebElement checkout;

	@FindBy(id = "continue-shopping")
	@CacheLookup
	private WebElement continueShopping;

	public List<SauceItem> getItems() {
		return cartItems.stream().map(el -> new SauceItem(el)).toList();
	}

	public void checkout() {
		waitUntilVisible(checkout).click();
	}

	public void continueShopping() {
		waitUntilVisible(continueShopping).click();
	}
	/**
	 *
	 * @return the element identifying the page
	 */
	public WebElement getTitle() {
		waitUntilVisible(title);
		return title;
	}
}

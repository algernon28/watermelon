package com.watermelon.pages;

import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;

public class Header extends SauceLabsSection{

	@FindBy(id = "react-burger-menu-btn")
	@CacheLookup
	private WebElement openMenu;

	@FindBy(className = "shopping_cart_link")
	@CacheLookup
	private WebElement shoppingCart;

	@FindBy(className = "app_logo")
	@CacheLookup
	private WebElement appLogo;

	@Inject
	public Header(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public void openShoppingCart() {
		waitUntilVisible(shoppingCart);
		shoppingCart.click();
	}

	public int getBadgeCount() {
		//wait for the cart being displayed first
		waitUntilVisible(shoppingCart);
		By badgeLocator = By.className("shopping_cart_badge");
		String count = "0";
		Optional<WebElement> badge = isPresent(badgeLocator);
		if(badge.isPresent()) {
			count = badge.get().getText();
		}
		return Integer.parseInt(count);
	}

	@Override
	public WebElement getTitle() {
		waitUntilVisible(appLogo);
		return appLogo;
	}

	@Override
	public boolean isLoaded() {
		WebElement pageTitle = getTitle();
		waitUntilVisible(pageTitle);
		return pageTitle.isDisplayed();
	}

}

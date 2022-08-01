package com.watermelon.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;

public class SideMenu extends SauceLabsSection {

	@FindBy(id = "react-burger-cross-btn")
	@CacheLookup
	private WebElement closeMenu;

	@FindBy(id = "inventory_sidebar_link")
	@CacheLookup
	private WebElement allItems;

	@FindBy(id = "about_sidebar_link")
	@CacheLookup
	private WebElement about;

	@FindBy(id = "logout_sidebar_link")
	@CacheLookup
	private WebElement logout;

	@FindBy(id = "reset_sidebar_link")
	@CacheLookup
	private WebElement resetAppState;

	@Inject
	public SideMenu(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	@Override
	public WebElement getTitle() {
		return allItems;
	}

}

package com.saucedemo.pages;

import com.watermelon.core.WebPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class SauceLabsSection extends WebPage {

	public SauceLabsSection(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

}

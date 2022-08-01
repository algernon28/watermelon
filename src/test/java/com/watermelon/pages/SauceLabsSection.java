package com.watermelon.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.watermelon.core.WebPage;

public abstract class SauceLabsSection extends WebPage {

	public SauceLabsSection(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

}

package com.watermelon.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.core.WebPage;

import lombok.Getter;

@Getter
public abstract class SauceLabsPage extends WebPage {
	@Inject
	protected Header header;

	public SauceLabsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

}

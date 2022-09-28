package com.saucedemo.web.pages;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.core.WebPage;

import lombok.Getter;

@Getter
public abstract class SauceLabsPage extends WebPage {
	@Inject
	protected Header header;

	@Inject
	public SauceLabsPage(RemoteWebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

}

package com.saucedemo.pages;

import com.google.inject.Inject;
import com.watermelon.core.WebPage;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public abstract class SauceLabsPage extends WebPage {
	@Inject
	protected Header header;

	public SauceLabsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

}

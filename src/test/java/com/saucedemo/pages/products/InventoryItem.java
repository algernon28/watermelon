package com.saucedemo.pages.products;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.saucedemo.pages.SauceItem;


public class InventoryItem extends SauceItem {

	private WebElement item;

	private By image = By.tagName("img");

	public InventoryItem(WebElement item) {
		super(item);
	}

	public String getImageSrcFileName() {
		return item.findElement(image).getAttribute("src");
	}

}

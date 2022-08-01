package com.watermelon.pages;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SauceItem implements Comparable<SauceItem> {

	protected WebElement item;

	protected By name = By.className("inventory_item_name");

	protected By description = RelativeLocator.with(By.className("inventory_item_desc")).below(name);

	protected By price = By.className("inventory_item_price");

	protected By btnAdd = By.tagName("button");

	public SauceItem(WebElement item) {
		this.item = item;
	}

	public String getDescription() {
		return item.findElement(description).getText();
	}

	@SneakyThrows
	public double getPrice() {
		String lblPrice = item.findElement(price).getText();
		log.debug("lblPrice = {}", lblPrice);
		double result = NumberFormat.getCurrencyInstance(Locale.US).parse(lblPrice).doubleValue();
		log.debug("price = {}", result);
		return result;
	}

	public String getName() {
		return item.findElement(name).getText();
	}

	/**
	 * Click the button to remove, if the label is "add to cart"
	 *
	 * @return the button label
	 */
	public String add() {
		WebElement button = item.findElement(btnAdd);
		String label = button.getText();
		if ("add to cart".equalsIgnoreCase(label)) {
			button.click();
		}
		return label;
	}

	/**
	 * Click the button to remove, if the label is "remove"
	 *
	 * @return the button label
	 */
	public String remove() {
		WebElement button = item.findElement(btnAdd);
		String label = button.getText();
		if ("remove".equalsIgnoreCase(label)) {
			button.click();
		}
		return label;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SauceItem))
			return false;
		SauceItem other = (SauceItem) obj;
		return Objects.equals(name, other.name) && Objects.equals(price, other.price);
	}

	@Override
	public int compareTo(SauceItem other) {
		return this.getName().compareTo(other.getName());
	}

}

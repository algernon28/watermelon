package com.watermelon.pages.products;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.pages.SauceLabsPage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductPage extends SauceLabsPage {

	public static String PROBLEM_SOURCE_IMAGEFILE = "/static/media/sl-404.168b1cce.jpg";

	@Inject
	public ProductPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	@FindBys({ @FindBy(xpath = "//span[@class='title']"), @FindBy(xpath = "//span[normalize-space()='Products']") })
	private WebElement title;

	@FindBy(className = "inventory_item")
	private List<WebElement> items;

	@FindBy(className = "product_sort_container")
	private WebElement sorter;

	public enum SORT_METHOD {
		AZ("Name (A to Z)"), ZA("Name (Z to A)"), LOHI("Price (low to high)"), HILO("Price (high to low)");

		public String label;

		private SORT_METHOD(String label) {
			this.label = label;

		}
	}

	public List<InventoryItem> getItems() {
		return items.stream().map(el -> new InventoryItem(el)).unordered().toList();
	}

	public List<InventoryItem> getItems(SORT_METHOD method) {
		Comparator<InventoryItem> compByName = Comparator.comparing(InventoryItem::getName);
		Comparator<InventoryItem> compByPrice = Comparator.comparing(InventoryItem::getPrice);
		List<InventoryItem> result = List.of();
		switch (method) {
		case AZ -> result = items.stream().map(el -> new InventoryItem(el)).sorted(compByName).toList();
		case ZA -> result = items.stream().map(el -> new InventoryItem(el)).sorted(compByName.reversed()).toList();
		case LOHI -> result = items.stream().map(el -> new InventoryItem(el)).sorted(compByPrice).toList();
		case HILO -> result = items.stream().map(el -> new InventoryItem(el)).sorted(compByPrice.reversed()).toList();
		}
		return result;
	}

	public List<InventoryItem> getItems(String method) {
		SORT_METHOD sortMethod = findSortMethodValue(method);
		return getItems(sortMethod);
	}

	private SORT_METHOD findSortMethodValue(String method) {
		Map<String, SORT_METHOD> sortMap = new HashMap<>();
		for (SORT_METHOD s : SORT_METHOD.values()) {
			sortMap.put(s.label, s);
		}
		return sortMap.get(method);
	}

	/**
	 *
	 * @param method the sort method (display value)
	 * @return the text of the active option
	 */
	public String sortItemsBy(String method) {
		waitUntilVisible(sorter);
		Select filter = new Select(sorter);
		filter.selectByVisibleText(method);
		WebElement activeOption = driver.findElement(By.className("active_option"));
		return activeOption.getText();
	}

	public String sortItemsBy(SORT_METHOD method) {
		waitUntilVisible(sorter);
		Select filter = new Select(sorter);
		filter.selectByValue(method.name().toLowerCase());
		return getSortValue();
	}

	/**
	 *
	 * @return the element identifying the page
	 */
	public WebElement getTitle() {
		waitUntilVisible(title);
		return title;
	}
	
	/**
	 *
	 * @return the text of the active option
	 */
	public String getSortValue() {
		String value = driver.findElement(By.className("active_option")).getText();
		log.debug("Active filter selection: {}", value);
		return value;
	}

}

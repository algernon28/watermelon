package com.saucedemo.web.steps.products;

import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.saucedemo.web.pages.Header;
import com.saucedemo.web.pages.products.InventoryItem;
import com.saucedemo.web.pages.products.ProductPage;
import com.saucedemo.web.pages.products.ProductPage.SORT_METHOD;
import com.watermelon.steps.BaseSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductSteps extends BaseSteps {

	public static final String WRONG_LOGIN = "login.wrong";
	public static final String LOCKEDOUT_LOGIN = "missing.domain";

	private ProductPage productPage;

	private Header headerPage;

	@Inject
	public ProductSteps(ProductPage productPage, Header headerPage) {
		super();
		this.productPage = productPage;
		this.headerPage = headerPage;
	}

	@Then("the number of products presented is {int}")
	public void the_number_of_products_is(int count) {
		int actualCount = productPage.getItems().size();
		then(actualCount).as("The number of item does not match").isEqualTo(count);
	}

	@And("I sort the products")
	public void i_sort_the_items(DataTable data) {
		List<Map<String, String>> methods = data.entries();
		methods.forEach(row -> {
			String sortText = row.get("sort value");
			SORT_METHOD sortMethod = SORT_METHOD.valueOf(sortText.toUpperCase());
			String currentSortMethod = productPage.sortItemsBy(sortMethod);
			then(currentSortMethod).as("The filter element did not update").isEqualTo(sortMethod.label.toUpperCase());
			sortItems(sortMethod.label);
		});

	}

	private void sortItems(String method) {
		log.debug("sorted!");
		List<InventoryItem> actualItems = productPage.getItems();
		List<InventoryItem> expectedItems = productPage.getItems(method);
		then(actualItems).as("Products were not correctly ordered").isEqualTo(expectedItems);
	}

	@And("I land on the Products page")
	public void i_land_on_products_page() {
		then(productPage).as("Products page did not show").returns(true, from(ProductPage::isLoaded));

	}

	@And("the cart badge shows {int}")
	public void cart_badge_shows_int(int value) {
		int badgeCount = headerPage.getBadgeCount();
		if (value > 0) {
			then(badgeCount).withFailMessage("There was no badge displayed").isNotZero();
		}
		then(badgeCount).as("The badge on the cart displayed wrong value").isEqualTo(value);
	}

	@Then("the cart badge shows nothing")
	public void cart_badge_shows_nothing() {
		cart_badge_shows_int(0);
	}

	@And("I add products")
	public void i_add_products(DataTable data) {
		String entryString = data.asList().stream().findFirst().get();
		i_add_products(entryString);
	}

	@And("I add {string} as products")
	public void i_add_products(String items) {
		List<String> entries = List.of(items.split(","));
		List<InventoryItem> products = productPage.getItems().stream().filter(p -> {
			String name = p.getName();
			return entries.contains(name);
		}).toList();
		addOrRemove(products, true);
	}


	@And("I remove products")
	public void i_remove_products(DataTable data) {
		addOrRemove(data, false);
	}

	private void addOrRemove(List<InventoryItem> items, boolean add) {
		if (add) {
			items.forEach(InventoryItem::add);
		} else {
			items.forEach(InventoryItem::remove);
		}

	}

	private void addOrRemove(DataTable data, boolean add) {
		List<String> entries = data.asList();
		List<InventoryItem> products = productPage.getItems().stream().filter(p -> entries.contains(p.getName()))
				.toList();
		addOrRemove(products, add);
	}

}
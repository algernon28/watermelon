package com.saucedemo.web.steps.checkout;

import static com.watermelon.core.assertions.WatermelonAssertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;

import com.google.inject.Inject;
import com.saucedemo.web.pages.CartPage;
import com.saucedemo.web.pages.Header;
import com.saucedemo.web.pages.SauceItem;
import com.saucedemo.web.pages.checkout.CheckoutCompletePage;
import com.saucedemo.web.pages.checkout.CheckoutInfo;
import com.saucedemo.web.pages.checkout.CheckoutOverview;
import com.watermelon.core.Utils;
import com.watermelon.steps.BaseSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;

public class CheckoutSteps extends BaseSteps {
	@Inject
	private CheckoutInfo checkoutInfo;

	@Inject
	private CheckoutOverview overviewPage;

	@Inject
	private CheckoutCompletePage checkoutComplete;

	@Inject
	private CartPage cartPage;

	@Inject
	private Header headerPage;

	public CheckoutSteps() {
		super();
	}

	@And("I enter the cart page")
	public void i_enter_the_cart_page() {
		headerPage.openShoppingCart();
	}

	@Then("the cart is empty")
	public void the_cart_is_empty() {
		then(cartPage.getItems()).isEmpty();
	}

	@And("I continue shopping")
	public void i_continue_shopping() {
		cartPage.continueShopping();
	}

	@And("the items in the cart are {int}")
	public void items_in_the_cart_are(int value) {
		int actual = cartPage.getItems().size();
		then(actual).isEqualTo(value);
	}

	@But("I remove {string}")
	public void i_remove(String items) {
		List<String> entries = List.of(items.split(","));
		List<SauceItem> products = cartPage.getItems().stream().filter(p -> {
			String name = p.getName();
			return entries.contains(name);
		}).toList();
		products.forEach(SauceItem::remove);
	}

	@And("I check out the products")
	public void checkOut() {
		i_enter_the_cart_page();
		cartPage.checkout();
		then(checkoutInfo).as("Should have landed on Checkout info page").returns(true, CheckoutInfo::isLoaded);
	}

	@And("I fill in my info")
	public void i_continue_checkout(DataTable data) {
		Map<String, String> entries = data.entries().get(0);
		String firstName = entries.get("first name");
		String lastName = entries.get("last name");
		String zipCode = entries.get("zip code");
		checkoutInfo.fillInfo(firstName, lastName, zipCode);
		checkoutInfo.continueCheckout();
	}

	@And("the overview page is displayed")
	public void overview_is_displayed() {
		then(overviewPage).as("Should have landed on Overview page").returns(true, CheckoutOverview::isLoaded);
	}

	@And("the item prices {string} are correct")
	public void item_prices_are_correct(String prices) {
		List<Double> expectedPrices = Utils.csvToDoubleList(prices).stream().sorted().toList();
		List<Double> actualPrices = overviewPage.getItems().stream().map(val -> val.getPrice()).sorted().toList();
		then(actualPrices).as("The checked out prices do not match").isEqualTo(expectedPrices);
	}

	@And("the {string} as payment and {string} as shipping details are correct")
	public void payment_and_shipping_details_are_correct(String paymentInfo, String shippingInfo) {
		String actualPaymentInfo = overviewPage.getPaymentInfo();
		String actualShippingInfo = overviewPage.getShippingInfo();
		SoftAssertions softly = new SoftAssertions();
		softly.assertThat(actualPaymentInfo).as("The payment method does not match").isEqualTo(paymentInfo);
		softly.assertThat(actualShippingInfo).as("The shipping details do not match").isEqualTo(shippingInfo);
		softly.assertAll();

	}

	// It seems that the data from Examples table will not work with {double}, so I
	// resorted to strings and converted them...
	@And("the {string} as subtotal, {string} as tax, {string} as total details are correct")
	public void subtotals(String subtotal, String tax, String total) {
		Double expectedSubTotal = Double.parseDouble(subtotal);
		Double expectedTax = Double.parseDouble(tax);
		Double expectedTotal = Double.parseDouble(total);
		Double actualSubtotal = overviewPage.getSubTotalValue();
		Double actualTax = overviewPage.getTaxValue();
		Double actualTotal = overviewPage.getTotalValue();
		SoftAssertions softly = new SoftAssertions();
		softly.assertThat(actualSubtotal).isEqualTo(expectedSubTotal);
		softly.assertThat(actualTax).isEqualTo(expectedTax);
		softly.assertThat(actualTotal).isEqualTo(expectedTotal);
		softly.assertAll();
	}

	@And("I confirm the transaction and a confirmation page is displayed")
	public void i_finish_transaction() {
		overviewPage.finish();
		assertThat(checkoutComplete).isLoaded();
	}

}

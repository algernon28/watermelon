package com.saucedemo.steps.login;

import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;
import java.util.Optional;

import com.google.inject.Inject;
import com.saucedemo.pages.LoginPage;
import com.watermelon.steps.BaseSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class LoginSteps extends BaseSteps {

	@Inject
	private LoginPage loginPage;
	
	public LoginSteps() {
		super();
	}

	@Given("I am on the login page")
	public void i_land_on_the_login_page() throws Throwable {
		log.debug("Driver: {}", driver);
		log.debug("{}", driver.getCurrentUrl());
		then(loginPage).as("Login page did not show").returns(true, from(LoginPage::isLoaded));

	}

	@When("I enter username as {string} and password as {string}")
	public void i_enter_credentials(String username, String password) throws Throwable {
		log.debug("I enter credentials: {}/{}", username, password);
		loginPage.login(username, password);
	}

	@Then("I expect validation message as {string} is displayed")
	public void validation_message(String messageKey) throws Throwable {
		String expected = bundle.getString(messageKey);
		log.debug("I expect message: {}", expected);
		Optional<String> actual = loginPage.errorMessage();
		then(actual).withFailMessage("No error messages displayed").isPresent();
		then(actual).as("Wrong error message displayed").hasValue(expected);
	}

	@Given("I am logged as user")
	public void i_am_logged_as_user(DataTable data) throws Throwable {
		Map<String, String> credentials = data.entries().stream().findFirst().get();
		String username = credentials.get("username");
		String password = credentials.get("password");
		i_land_on_the_login_page();
		i_enter_credentials(username, password);
		String user = driver.manage().getCookieNamed("session-username").getValue();
		then(user).as("User is not logged with the right credentials").isEqualTo(username);
	}

}
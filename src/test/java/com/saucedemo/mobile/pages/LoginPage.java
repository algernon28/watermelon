package com.saucedemo.mobile.pages;

import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.core.MobilePage;
import com.watermelon.core.WebPage;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.cucumber.guice.ScenarioScoped;

@ScenarioScoped
public class LoginPage extends MobilePage {

	@AndroidFindBy(accessibility = "longpress reset app")
	private WebElement imgLoginLogo;

	@AndroidFindBy(accessibility = "Username input field")
	private WebElement txtUsername;

	@AndroidFindBy(accessibility = "Password input field")
	private WebElement txtPassword;

	@AndroidFindBy(accessibility = "Login button")
	private WebElement btnLogin;

	@FindBy(xpath = "//android.view.ViewGroup[@content-desc=\"Username-error-message\"]/android.widget.TextView")
	private WebElement errorMessageUsername;

	@FindBy(xpath = "//android.view.ViewGroup[@content-desc=\"Password-error-message\"]/android.widget.TextView")
	private WebElement errorMessagePassword;

	@Inject
	public LoginPage(AppiumDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	private void submit() {
		waitUntilVisible(btnLogin).click();
	}

	public void login(String username, String password) {
		typeCredentials(username, password);
		submit();
	}

	public void typeCredentials(String username, String password) {
		clearAll(txtUsername, txtPassword);
		type(txtUsername, username);
		type(txtPassword, password);
	}

	public void clearFields() {
		clearAll(txtUsername);
		this.txtUsername.clear();
		this.txtPassword.clear();
	}

	public Optional<String> errorMessage() {
		try {
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOf(errorMessageUsername),
					ExpectedConditions.visibilityOf(errorMessagePassword)));
		} catch (Exception e) {
			return Optional.empty();
		}
		return Optional.of("Username and Password are required");
	}

	@Override
	public WebElement getTitle() {
		waitUntilVisible(imgLoginLogo);
		return imgLoginLogo;
	}

}

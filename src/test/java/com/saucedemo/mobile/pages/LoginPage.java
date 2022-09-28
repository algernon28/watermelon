package com.saucedemo.mobile.pages;

import java.util.Optional;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.Inject;
import com.watermelon.core.MobilePage;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.cucumber.guice.ScenarioScoped;

@ScenarioScoped
public class LoginPage extends MobilePage {

	@AndroidFindBy(xpath = "//android.widget.ScrollView[@content-desc='test-Login']/android.view.ViewGroup/android.widget.ImageView[1]")
	private WebElement imgLoginLogo;

	@AndroidFindBy(accessibility = "test-Username")
	private WebElement txtUsername;

	@AndroidFindBy(accessibility = "test-Password")
	private WebElement txtPassword;

	@AndroidFindBy(accessibility = "test-LOGIN")
	private WebElement btnLogin;

	@AndroidFindBy(accessibility = "test-Error message")
	private WebElement errorMessage;

	
	@Inject
	public LoginPage(RemoteWebDriver driver, WebDriverWait wait) {
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
			waitUntilVisible(errorMessage);
		} catch (Exception e) {
			return Optional.empty();
		}
		return Optional.of(errorMessage.getText());
	}
	

	@Override
	public WebElement getTitle() {
		waitUntilVisible(imgLoginLogo);
		return imgLoginLogo;
	}

}

package com.watermelon.steps;

import com.google.inject.Inject;
import com.watermelon.core.di.modules.Configuration;
import com.watermelon.core.di.modules.Configuration.Reporting.LEVEL;
import com.watermelon.core.di.modules.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

import static com.google.common.net.MediaType.PLAIN_TEXT_UTF_8;
import static com.google.common.net.MediaType.PNG;

public class Hooks extends AbstractTestNGCucumberTests {
	@Inject
	WebDriver driver;

	@Inject
	Configuration config;

	@Inject
	Locale locale;

	@Before(order = 1)
	public void startUp() throws IOException, AWTException {
		driver.manage().window().maximize();
		driver.navigate().to(config.getServer().getURL());
	}

	@AfterStep
	public void screenshot(Scenario scenario) {
		LEVEL configLevel = config.getReporting().getScreenshotLevel();
		byte[] payload = takeScreenshot();
		String name = String.format("%s: %s", scenario.getName(), scenario.getSourceTagNames());
		if (configLevel == LEVEL.ALWAYS) {
			scenario.attach(payload, "image/png", name.concat(": SUCCESS"));
		} else if (configLevel == LEVEL.ONLY_FAILED && scenario.isFailed()) {
			scenario.attach(payload, "image/png", name.concat(": FAIL"));
		}
	}

	private byte[] readImage(String fileName) throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("img/" + fileName);
		return is.readAllBytes();
	}

	@Before(order = 1000)
	public void attachData(Scenario scenario) throws IOException {
		RemoteWebDriver rd = (RemoteWebDriver) driver;
		String browser = rd.getCapabilities().getBrowserName();
		String version = rd.getCapabilities().getBrowserVersion();
		String message = String.format("Browser: %s [%s], Driver: %s, Language: %s", browser, version,
				rd.getClass().getSimpleName(), locale.getLanguage());
		switch (browser) {
		case "chrome" -> scenario.attach(readImage("chrome.png"), PNG.toString(), message);
		case "firefox" -> scenario.attach(readImage("firefox.png"), PNG.toString(), message);
		case "opera" -> scenario.attach(readImage("opera.png"), PNG.toString(), message);
		case "safari" -> scenario.attach(readImage("safari.png"), PNG.toString(), message);
		default -> scenario.attach("Unknown Browser", PLAIN_TEXT_UTF_8.toString(), message);
		}
	}

	private byte[] takeScreenshot() {
		TakesScreenshot screenshotter = (TakesScreenshot) driver;
		return screenshotter.getScreenshotAs(OutputType.BYTES);
	}

	@After(order = 10000)
	public void tearDown() {
		if (Optional.ofNullable(driver).isPresent()) {
			driver.quit();
			DriverManager.removeDriver();
		}
	}

}

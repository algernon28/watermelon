package com.watermelon.web.hooks;

import static com.google.common.net.MediaType.PLAIN_TEXT_UTF_8;
import static com.google.common.net.MediaType.PNG;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.inject.Inject;
import com.watermelon.core.di.modules.DriverManager;
import com.watermelon.core.di.modules.MapConfiguration;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;

public class Hooks extends AbstractTestNGCucumberTests {
	@Inject
	private RemoteWebDriver driver;

	@Inject
	private MapConfiguration<String, Object> config;

	private static final List<String> supportedBrowsers = List.of("chrome", "firefox", "opera", "safari");

	public enum LEVEL {
		ALWAYS, ONLY_FAILED
	}

	@Inject
	Locale locale;

	@Before(order = 1)
	public void startUp() throws IOException {
		driver.manage().window().maximize();
		Map<String, Object> server = config.getMap("server");
		String protocol = (String) server.get("protocol");
		String host = (String) server.get("host");
		int port = (Integer) server.get("port");
		String resource = (String) server.get("resource");
		driver.navigate().to(new URL(protocol, host, port, resource));
	}

	@AfterStep
	public void screenshot(Scenario scenario) {
		LEVEL configLevel = LEVEL.valueOf((String) config.getMap("reporting").get("screenshotLevel"));
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
		if (supportedBrowsers.contains(browser)) {
			String imageName = browser.concat(".png");
			scenario.attach(readImage(imageName), PNG.toString(), message);
		} else {
			scenario.attach("Unknown Browser", PLAIN_TEXT_UTF_8.toString(), message);
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

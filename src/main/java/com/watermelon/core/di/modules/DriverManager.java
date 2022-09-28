package com.watermelon.core.di.modules;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;
import org.testng.xml.XmlTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.watermelon.core.UnsupportedBrowserException;
import com.watermelon.core.di.modules.Configuration.AppiumServer;
import com.watermelon.core.driver.ChromeManager;
import com.watermelon.core.driver.EdgeManager;
import com.watermelon.core.driver.FireFoxManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.guice.ScenarioScoped;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;

/**
 * Service Provider: provides a thread-safe instance of {@link WebDriver} for
 * Dependency Injection
 * 
 * @see Provider
 * @see WebDriver
 * @author AM
 *
 */
@ScenarioScoped
public class DriverManager implements Provider<RemoteWebDriver> {

	private static final ThreadLocal<RemoteWebDriver> DRIVERPOOL = new ThreadLocal<>();
	private Configuration config;

	@Inject
	public DriverManager(Configuration config) throws UnsupportedBrowserException, IOException {
		this.config = config;
		RemoteWebDriver driver;
		XmlTest context = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		String myBrowser = context.getParameter("browser");
		Optional<String> platform = Optional.ofNullable(context.getParameter("platform"));
		if (platform.isPresent()) {
			driver = resolveAppiumDriver(context, platform.get());
		} else {
			driver = (RemoteWebDriver) resolveWebDriver(context, myBrowser);
		}
		DRIVERPOOL.set(driver);
	}

	private AppiumDriver resolveAppiumDriver(XmlTest context, String platform) throws IOException {
		AppiumServer appium = config.getAppiumServer();
		URL url = new URL("http", appium.getHost(), appium.getPort(), appium.getPath());
		Path capsPath = Paths.get("src/test/resources/capabilities", context.getParameter("caps"));
		Capabilities caps = loadCapabilities(capsPath);
		switch (platform.toLowerCase()) {
		case "android":
			return new AndroidDriver(url, caps);
		case "ios":
			return new IOSDriver(url, caps);
		default:
			throw new IllegalArgumentException("Unexpected platform: " + platform);
		}
	}

	private Capabilities loadCapabilities(Path jsonPath) throws IOException {
		URL fileURL = jsonPath.toAbsolutePath().toUri().toURL();
		@SuppressWarnings("unchecked")
		Map<String, ?> map = new ObjectMapper().readValue(fileURL, HashMap.class);
		return new DesiredCapabilities(map);
	}

	private WebDriver resolveWebDriver(XmlTest context, String browserName) throws UnsupportedBrowserException {
		WebDriver driver;
		DriverManagerType browser = DriverManagerType.valueOf(browserName.toUpperCase());
		switch (browser) {
		case CHROME, CHROMIUM:
			driver = new ChromeManager(context.getAllParameters()).getDriver();
			break;
		case FIREFOX:
			Map<String, String> parameters = context.getAllParameters();
			String githubToken = config.getGithubToken();
			parameters.put(FireFoxManager.GITHUB_TOKEN, githubToken);
			driver = new FireFoxManager(parameters).getDriver();
			break;
		case EDGE:
			driver = new EdgeManager(context.getAllParameters()).getDriver();
			break;
		default:
			String msg = String.format("Browser type [%s] not recognised", browser);
			throw new WebDriverManagerException(msg);
		}
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		return driver;
	}

	/**
	 * Remove current driver from the threadpool. Delegate method to comply with
	 * Sonar {@code java:S5164} rule.
	 */
	public static void removeDriver() {
		DRIVERPOOL.remove();
	}

	/**
	 *
	 * @return the Selenium {@link WebDriver} instance
	 */
	@Override
	public RemoteWebDriver get() {
		return DRIVERPOOL.get();
	}

}

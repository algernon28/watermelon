package com.watermelon.core.di.modules;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.xml.XmlTest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.watermelon.core.UnsupportedBrowserException;
import com.watermelon.core.driver.ChromeManager;
import com.watermelon.core.driver.EdgeManager;
import com.watermelon.core.driver.FireFoxManager;

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
public class DriverManager implements Provider<WebDriver> {

	private static final ThreadLocal<WebDriver> DRIVERPOOL = new ThreadLocal<>();

	@Inject
	public DriverManager(Configuration config) throws UnsupportedBrowserException {
		XmlTest context = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		String myBrowser = context.getParameter("browser");
		DriverManagerType browser = DriverManagerType.valueOf(myBrowser.toUpperCase());
		WebDriver driver;
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
		DRIVERPOOL.set(driver);
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
	public WebDriver get() {
		return DRIVERPOOL.get();
	}

}

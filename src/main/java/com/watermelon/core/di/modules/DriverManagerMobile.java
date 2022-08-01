package com.watermelon.core.di.modules;

import static com.watermelon.core.Utils.DEFAULT_BROWSER;

import java.time.Duration;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;
import org.testng.xml.XmlTest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.watermelon.core.Utils;

import io.cucumber.guice.ScenarioScoped;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.config.OperatingSystem;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;

/**
 * Service Provider: provides a thread-safe instance of {@link WebDriver} for Dependency Injection
 * @see Provider
 * @see WebDriver
 * @author AM
 *
 */
@ScenarioScoped
public class DriverManagerMobile implements Provider<WebDriver> {
	private static final Logger logger = LoggerFactory.getLogger(DriverManagerMobile.class);

	private static final ThreadLocal<WebDriver> DRIVERPOOL = new ThreadLocal<>();

	@Inject
	public DriverManagerMobile(Configuration config) {
		XmlTest context = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		Optional<String> myVersion = Utils.lookupParameter("version", context);
		Optional<String> myLanguage = Utils.lookupParameter("language", context);
		Optional<String> myCountry = Utils.lookupParameter("country", context);
		Optional<String> myOS = Utils.lookupParameter("os", context);
		String myBrowser = Utils.lookupParameter("browser", context).orElse(DEFAULT_BROWSER);
		DriverManagerType browser = DriverManagerType.valueOf(myBrowser.toUpperCase());
		WebDriverManager wdm;

		WebDriver driver;
		String arg;
		switch (browser) {
		case CHROME:
			wdm = WebDriverManager.chromedriver();
			myVersion.ifPresent(v -> wdm.browserVersion(myVersion.get()));
			myOS.ifPresent(
					os -> wdm.operatingSystem(OperatingSystem.valueOf(myOS.get())));
			ChromeOptions chOptions = new ChromeOptions();
			arg = String.format("--lang=%s_%s", myLanguage, myCountry);
			chOptions.addArguments(arg).setAcceptInsecureCerts(true).setCapability(ChromeOptions.CAPABILITY, chOptions);
			wdm.clearResolutionCache();
			driver = wdm.capabilities(chOptions).create();
			logger.debug("ChromeDriver built: {}", driver);
			break;
		case FIREFOX:
			wdm = WebDriverManager.firefoxdriver();
			String githubToken = config.getGithubToken();
			myVersion.ifPresent(v -> wdm.browserVersion(myVersion.get()));
			myOS.ifPresent(os -> wdm.operatingSystem(OperatingSystem.valueOf(myOS.get())));
			ProfilesIni allProfiles = new ProfilesIni();
			FirefoxProfile profile = allProfiles.getProfile("default");
			FirefoxOptions ffOptions = new FirefoxOptions();
			ffOptions.addPreference("intl.accept_languages", myLanguage);
			ffOptions.setProfile(profile).setAcceptInsecureCerts(true);
			wdm.clearResolutionCache();
			driver = wdm.gitHubToken(githubToken)
					// can't make it work with FirefoxOptions yet, commenting for now...
					//.capabilities(ffOptions)
					.create();
			logger.debug("FireFoxDriver built: {}", driver);
			break;
		case EDGE:
			wdm = WebDriverManager.edgedriver();
			myVersion.ifPresent(v -> wdm.browserVersion(myVersion.get()));
			myOS.ifPresent(os -> wdm.operatingSystem(OperatingSystem.valueOf(myOS.get())));
			EdgeOptions eOptions = new EdgeOptions();
			arg = String.format("--lang=%s", myLanguage);
			eOptions.addArguments(arg).setAcceptInsecureCerts(true);
			wdm.clearResolutionCache();
			driver = wdm.capabilities(eOptions).create();
			logger.debug("EdgeDriver built: {}", driver);
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

package com.watermelon.mobile.hooks;

import static com.google.common.net.MediaType.PNG;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.inject.Inject;
import com.saucedemo.mobile.misc.BundleID;
import com.watermelon.core.di.modules.DriverManager;
import com.watermelon.core.di.modules.MapConfiguration;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;

public class MobileHooks extends AbstractTestNGCucumberTests {
	@Inject
	private RemoteWebDriver driver;

	@Inject
	private MapConfiguration<String, Object> config;

	public enum LEVEL {
		ALWAYS, ONLY_FAILED
	}

	@Inject
	Locale locale;

	@Before(order = 1)
	public void startUp() throws IOException {
		AppiumDriver ap = (AppiumDriver)driver;
		InteractsWithApps ad = ((InteractsWithApps) driver);
		// ad.installApp((String) driver.getCapabilities().getCapability("appium:app"));
		String platform = driver.getCapabilities().getPlatformName().name();
//		switch (platform) {
//		case "ANDROID" -> ad.activateApp(BundleID.ANDROID.id);
//		case "IOS" -> ad.activateApp(BundleID.IOS.id);
//		default -> throw new IllegalArgumentException("Unexpected platform: " + platform);
//		}
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
		RemoteWebDriver rd = driver;
		String platform = rd.getCapabilities().getPlatformName().name();
		String version = rd.getCapabilities().getBrowserVersion();
		String message = String.format("Platform: %s [%s], Driver: %s", platform, version,
				rd.getClass().getSimpleName());
		String imageName = platform.toLowerCase().concat(".png");
		scenario.attach(readImage(imageName), PNG.toString(), message);
	}

	private byte[] takeScreenshot() {
		TakesScreenshot screenshotter = driver;
		return screenshotter.getScreenshotAs(OutputType.BYTES);
	}

	@After(order = 10000)
	public void tearDown() {
		if (Optional.ofNullable(driver).isPresent()) {
			if (driver instanceof InteractsWithApps iwa) {
				iwa.removeApp(getAppBundleId());
			}
			driver.quit();
			DriverManager.removeDriver();
		}
	}

	private String getAppBundleId() {
		if (driver instanceof InteractsWithApps) {
			switch (driver.getCapabilities().getPlatformName().name()) {
			case "ANDROID":
				return BundleID.ANDROID.id;
			case "IOS":
				return BundleID.IOS.id;
			default:
				return null;
			}
		}
		return null;
	}

}

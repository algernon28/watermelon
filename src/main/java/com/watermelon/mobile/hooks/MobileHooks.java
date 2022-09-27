package com.watermelon.mobile.hooks;

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

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;

public class MobileHooks extends AbstractTestNGCucumberTests {
	@Inject
	private RemoteWebDriver driver;

	private AppiumDriverLocalService appiumService;

	@Inject
	private MapConfiguration<String, Object> config;

	public enum LEVEL {
		ALWAYS, ONLY_FAILED
	}

	@Inject
	Locale locale;

	@Before(order = 1)
	public void startUp() throws IOException {
		Map<String, Object> server = config.getMap("server");
		String protocol = (String) server.get("protocol");
		String host = (String) server.get("host");
		int port = (Integer) server.get("port");
		String resource = (String) server.get("resource");
		driver.navigate().to(new URL(protocol, host, port, resource));
		appiumService = buildAppiumService();
		appiumService.start();
	}

	private AppiumDriverLocalService buildAppiumService() {
		AppiumServiceBuilder builder = new AppiumServiceBuilder();
		Map<String, Object> appiumServer = config.getMap("appiumServer");
		Optional<Object> ip = Optional.of(appiumServer.get("host"));
		Optional<Object> path = Optional.of(appiumServer.get("path"));
		Optional<Object> port = Optional.of(appiumServer.get("port"));
		builder.withIPAddress(ip.get().toString().concat(path.get().toString())).usingPort((int) port.get())
				.withArgument(GeneralServerFlag.USE_DRIVERS, "uiautomator2,xcuitest")
				.withArgument(GeneralServerFlag.LOG_LEVEL, "error")
				.withEnvironment(Map.of("PATH", System.getenv("PATH")));
		return builder.build();
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
		String platform = rd.getCapabilities().getPlatformName().name();
		String version = rd.getCapabilities().getBrowserVersion();
		String message = String.format("Platform: %s [%s], Driver: %s", platform, version,
				rd.getClass().getSimpleName());
		String imageName = platform.concat(".png");
		scenario.attach(readImage(imageName), PNG.toString(), message);
	}

	private byte[] takeScreenshot() {
		TakesScreenshot screenshotter = driver;
		return screenshotter.getScreenshotAs(OutputType.BYTES);
	}

	@After(order = 10000)
	public void tearDown() {
		if (Optional.ofNullable(driver).isPresent()) {
			driver.quit();
			DriverManager.removeDriver();
			appiumService.stop();
		}
	}

}

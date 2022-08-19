package com.watermelon.core.driver;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.watermelon.core.UnsupportedBrowserException;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.OperatingSystem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChromeManager extends AbstractManager<ChromeDriver>{
	
	public ChromeManager(Map<String, String> parameters) throws UnsupportedBrowserException {
		super(parameters);
		String type = this.getClass().getTypeName();
		if (myBrowser.isEmpty() || !"Chrome".equalsIgnoreCase(myBrowser)) {
			throw new UnsupportedBrowserException(String.format("Wrong browser parameter [%s] for [%s]", myBrowser, type));
		}
		wdm = WebDriverManager.chromedriver();
		
	}

	@Override
	public ChromeDriver getDriver() {
		myVersion.ifPresent(v -> wdm.browserVersion(myVersion.get()));
		myOS.ifPresent(
				os -> wdm.operatingSystem(OperatingSystem.valueOf(myOS.get())));
		ChromeOptions chOptions = new ChromeOptions();
		String arg = String.format("--lang=%s_%s", myLanguage, myCountry);
		chOptions.addArguments(arg).setAcceptInsecureCerts(true).setCapability(ChromeOptions.CAPABILITY, chOptions);
		wdm.clearResolutionCache();
		driver = wdm.capabilities(chOptions).create();
		log.debug("ChromeDriver built: {}", driver);
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		return (ChromeDriver) driver;
	}

}

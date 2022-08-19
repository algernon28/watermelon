package com.watermelon.core.driver;

import java.util.Map;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import com.watermelon.core.UnsupportedBrowserException;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.OperatingSystem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EdgeManager extends AbstractManager<EdgeDriver>{
	
	public EdgeManager(Map<String, String> parameters) throws UnsupportedBrowserException {
		super(parameters);
		String type = this.getClass().getTypeName();
		if (myBrowser.isEmpty() || !"Edge".equalsIgnoreCase(myBrowser)) {
			throw new UnsupportedBrowserException(String.format("Wrong browser parameter [%s] for [%s]", myBrowser, type));
		}
		wdm = WebDriverManager.edgedriver();
		
	}

	@Override
	public EdgeDriver getDriver() {
		myVersion.ifPresent(v -> wdm.browserVersion(myVersion.get()));
		myOS.ifPresent(os -> wdm.operatingSystem(OperatingSystem.valueOf(myOS.get())));
		EdgeOptions eOptions = new EdgeOptions();
		String arg = String.format("--lang=%s", myLanguage);
		eOptions.addArguments(arg).setAcceptInsecureCerts(true);
		wdm.clearResolutionCache();
		driver = wdm.capabilities(eOptions).create();
		log.debug("EdgeDriver built: {}", driver);	
		return (EdgeDriver) driver;
	}

}

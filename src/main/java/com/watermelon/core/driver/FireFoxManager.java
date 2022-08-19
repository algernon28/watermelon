package com.watermelon.core.driver;

import java.util.Map;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

import com.watermelon.core.UnsupportedBrowserException;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.OperatingSystem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FireFoxManager extends AbstractManager<FirefoxDriver>{
	
	public static final String GITHUB_TOKEN = "githubToken";
	private String githubToken;
	
	public FireFoxManager(Map<String, String> parameters) throws UnsupportedBrowserException {
		super(parameters);
		String type = this.getClass().getTypeName();
		if (myBrowser.isEmpty() || !"FireFox".equalsIgnoreCase(myBrowser)) {
			throw new UnsupportedBrowserException(String.format("Wrong browser parameter [%s] for [%s]", myBrowser, type));
		}
		wdm = WebDriverManager.firefoxdriver();
		this.githubToken = parameters.get(GITHUB_TOKEN);
	}

	@Override
	public FirefoxDriver getDriver() {
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
		log.debug("FirefoxDriver built: {}", driver);		
		return (FirefoxDriver) driver;
	}

}

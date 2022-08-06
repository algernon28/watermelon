package com.saucedemo.steps;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "classpath:features", glue = {
		"com.watermelon.core.di.injectors",
		"com.watermelon.steps" },
monochrome = true,
plugin = { "pretty",
				"html:target/TestReports/cucumber/chrome/report.html",
				"timeline:target/TestReports/cucumber/chrome/timeline",
				"json:target/TestReports/cucumber/cucumber-chrome.json",
				"testng:target/TestReports/cucumber/cucumber-chrome.xml"})
@Slf4j
public class CucumberChromeRunner extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = false)
	public Object[][] scenarios() {
		log.debug("Scenarios");
		return super.scenarios();
	}

}

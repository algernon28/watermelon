package com.saucedemo.mobile.steps;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import lombok.extern.slf4j.Slf4j;

@CucumberOptions(features = "classpath:features/mobile", glue = {
		"com.watermelon.mobile.hooks",
		"com.saucedemo.mobile.steps" },
monochrome = true,
plugin = { "pretty",
				"html:target/TestReports/cucumber/android/report.html",
				"timeline:target/TestReports/cucumber/android/timeline",
				"json:target/TestReports/cucumber/cucumber-android.properties",
				"testng:target/TestReports/cucumber/cucumber-android.xml",
				"rerun:target/TestReports/cucumber/cucumber-android-rerun.txt"
				}
)
@Slf4j
public class AndroidRunner extends AbstractTestNGCucumberTests {
	
	@Override
	@DataProvider(parallel = false)
	public Object[][] scenarios() {
		log.debug("Scenarios");
		return super.scenarios();
	}

}

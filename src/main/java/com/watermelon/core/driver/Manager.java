package com.watermelon.core.driver;

import org.openqa.selenium.WebDriver;

public interface Manager<T extends WebDriver>{
	T getDriver();
}

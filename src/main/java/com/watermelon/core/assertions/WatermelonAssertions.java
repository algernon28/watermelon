package com.watermelon.core.assertions;

import com.watermelon.core.WebPage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;

public class WatermelonAssertions extends Assertions {
	public static WebElementAssert assertThat(WebElement actual) {
		return new WebElementAssert(actual);
	}

	public static WebElementAssert then(WebElement actual) {
		return new WebElementAssert(actual);
	}

	public static WebPageAssert assertThat(WebPage actual) {
		return new WebPageAssert(actual);
	}

	public static WebPageAssert then(WebPage actual) {
		return new WebPageAssert(actual);
	}
}

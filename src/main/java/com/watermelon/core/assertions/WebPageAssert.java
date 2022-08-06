package com.watermelon.core.assertions;

import com.watermelon.core.WebPage;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.description.Description;

public class WebPageAssert extends AbstractAssert<WebPageAssert, WebPage> {

	protected WebPageAssert(WebPage actual) {
		super(actual, WebPageAssert.class);
	}

	public WebPageAssert isLoaded() {
		isNotNull();
		if (!actual.isLoaded()) {
			failWithMessage("Expected page to be displayed. But was not");
		}
		return this;
	}

	@Override
	public WebPageAssert as(Description description) {
		super.as(description);
		return this;
	}

	@Override
	public WebPageAssert describedAs(String description, Object... args) {
		super.describedAs(description, args);
		return this;
	}

	@Override
	public WebPageAssert describedAs(Description description) {
		super.describedAs(description);
		return this;
	}
}

package com.watermelon.core.webelements;

import org.openqa.selenium.WebElement;

/**
 * Wrap a checkbox Webelement, providing convenience methods to check/uncheck it
 *
 * @author AM
 *
 */
public class Checkbox {
	private final WebElement checkboxField;

	public Checkbox(final WebElement checkboxField) {
		this.checkboxField = checkboxField;
	}

	public void check() {
		if (!this.isChecked()) {
			checkboxField.click();
		}
	}

	public void uncheck() {
		if (this.isChecked()) {
			checkboxField.click();
		}
	}

	public boolean isChecked() {
		return checkboxField.isSelected();
	}

}

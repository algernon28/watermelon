package com.watermelon.core.webelements;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.WebElement;

/**
 * Wraps a list of radiobutton Webelements as a group
 *
 * @author AM
 *
 */
public class RadioButtonGroup {
	private final List<WebElement> radioButtons;

	public RadioButtonGroup(List<WebElement> radioButtons) {
		this.radioButtons = radioButtons;
	}

	/**
	 *
	 * @return the value of the selected button
	 */
	public Optional<String> getSelectedValue() {
		for (WebElement radioButton : radioButtons) {
			if (radioButton.isSelected()) {
				return Optional.of(radioButton.getAttribute("value"));
			}
		}
		return Optional.empty();
	}

	/**
	 * Select a radiobutton by its value
	 *
	 * @param value the value of the button to select
	 */
	public void selectByValue(String value) {
		for (WebElement radioButton : radioButtons) {
			if (value.equals(radioButton.getAttribute("value"))) {
				radioButton.click();
				break;
			}
		}
	}
}
package com.saucedemo.mobile.misc;

public enum BundleID {
	ANDROID("com.swaglabsmobileapp"), IOS("com.saucelabs.SwagLabsMobileApp");

	public String id;

	private BundleID(String name) {
		this.id = name;
	}
}
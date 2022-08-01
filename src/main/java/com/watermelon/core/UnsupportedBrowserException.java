package com.watermelon.core;

public class UnsupportedBrowserException extends Exception {

	private static final long serialVersionUID = 3168210740021354489L;

	public UnsupportedBrowserException() {
		super();
	}

	public UnsupportedBrowserException(String message) {
		super(message);
	}

	public UnsupportedBrowserException(Throwable cause) {
		super(cause);
	}

	public UnsupportedBrowserException(String message, Throwable cause) {
		super(message, cause);
	}

}

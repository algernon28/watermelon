package com.watermelon.core;

public class PageNotLoadedException extends RuntimeException {

	private static final long serialVersionUID = 3168210740021354489L;

	public PageNotLoadedException() {
		super();
	}

	public PageNotLoadedException(String message) {
		super(message);
	}

	public PageNotLoadedException(Throwable cause) {
		super(cause);
	}

	public PageNotLoadedException(String message, Throwable cause) {
		super(message, cause);
	}

}

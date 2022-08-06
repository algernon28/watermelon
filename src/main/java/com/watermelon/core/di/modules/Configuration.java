package com.watermelon.core.di.modules;

import lombok.Data;

import java.net.MalformedURLException;
import java.net.URL;

@Data
public class Configuration {
	private String githubToken;
	private Server server;
	private Reporting reporting;

	@Data
	public static class Server {
		private String protocol;
		private String host;
		private int port;
		private String resource;

		public URL getURL() throws MalformedURLException {
			return new URL(protocol, host, port, resource);
		}
	}

	@Data
	public static class Reporting {
		public enum LEVEL {
			ALWAYS, ONLY_FAILED
		}

		private LEVEL screenshotLevel;
	}
}

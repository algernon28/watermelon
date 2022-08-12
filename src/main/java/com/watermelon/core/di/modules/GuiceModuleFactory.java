package com.watermelon.core.di.modules;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.guice.CucumberModules;
import io.cucumber.guice.ScenarioScope;

public class GuiceModuleFactory implements ObjectFactory {
	private Injector injector;

	public GuiceModuleFactory() {
		this.injector = Guice.createInjector(Stage.DEVELOPMENT, CucumberModules.createScenarioModule(),
				new DriverManagerModule(), new ConfigurationModule());
	}

	@Override
	public boolean addClass(Class<?> glueClass) {
		return true;
	}

	@Override
	public <T> T getInstance(Class<T> glueClass) {
		return this.injector.getInstance(glueClass);
	}

	@Override
	public void start() {
		injector.getInstance(ScenarioScope.class).enterScope();

	}

	@Override
	public void stop() {
		injector.getInstance(ScenarioScope.class).exitScope();

	}

}

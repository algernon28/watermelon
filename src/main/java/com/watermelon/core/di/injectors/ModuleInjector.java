package com.watermelon.core.di.injectors;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.watermelon.core.di.modules.ConfigurationModule;
import com.watermelon.core.di.modules.DriverManagerModule;

import io.cucumber.guice.CucumberModules;
import io.cucumber.guice.InjectorSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModuleInjector implements InjectorSource {

	@Override
	public Injector getInjector() {
		Injector inj = Guice.createInjector(Stage.DEVELOPMENT, CucumberModules.createScenarioModule(),
				new DriverManagerModule(), new ConfigurationModule());
		log.debug("Injector: {}", inj);
		return inj;
	}

}

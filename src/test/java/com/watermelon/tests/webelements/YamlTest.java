package com.watermelon.tests.webelements;

import java.io.FileNotFoundException;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import com.watermelon.core.Utils;
import com.watermelon.core.di.modules.MapConfiguration;

public class YamlTest {

  @Test
  public void beforeTestTest() {
  }
  
  
  @Test
  public void testYamlBean() throws FileNotFoundException {
	  MapConfiguration<String,String> bean = Utils.fromYaml("config-dev.yaml");
	  SoftAssertions softly = new SoftAssertions();
	  softly.assertThat(bean).isNotNull();
	  softly.assertThat(bean).hasFieldOrProperty("server");
	  softly.assertAll();
  }
}

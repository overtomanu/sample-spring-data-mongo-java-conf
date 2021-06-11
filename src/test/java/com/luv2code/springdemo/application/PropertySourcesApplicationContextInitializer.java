package com.luv2code.springdemo.application;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertySourcesApplicationContextInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final Logger log = LoggerFactory
			.getLogger(PropertySourcesApplicationContextInitializer.class);

	@Override
	  public void initialize(ConfigurableApplicationContext applicationContext) {
			log.info("Adding some test property sources");
	    String[] profiles = applicationContext.getEnvironment().getActiveProfiles();
	    // ... Add property sources according to selected spring profile 
	    // (note there already are some property sources registered, system properties etc)
	    if(Arrays.asList(profiles).contains("test")) {
			PropertySource testPropertiesSource;
			try {
				Properties dbProperties = PropertiesLoaderUtils
						.loadAllProperties("embedded-mongo.properties");
				testPropertiesSource = new PropertiesPropertySource(
						"testProperties", dbProperties);
				applicationContext.getEnvironment().getPropertySources()
						.addFirst(testPropertiesSource);
			} catch (IOException e) {
				log.error("unable to load persistence-hsqldb.properties", e);
			}

	    }
	  }


}
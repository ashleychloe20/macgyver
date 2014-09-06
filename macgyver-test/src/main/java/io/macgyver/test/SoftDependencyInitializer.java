package io.macgyver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * DynamicInitializer exists so that we can avoid a cyclic dependency between
 * macgyver-core and macgyver-test.
 * 
 * @author rschoening
 *
 */
public class SoftDependencyInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {

	Logger logger = LoggerFactory.getLogger(SoftDependencyInitializer.class);

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {

		ApplicationContextInitializer<ConfigurableApplicationContext> ci = null;

		try {
		
			Class clazz = Class
					.forName("io.macgyver.core.SpringContextInitializer");
			ci = (ApplicationContextInitializer<ConfigurableApplicationContext>) clazz
					.newInstance();
		} catch (Exception e) {
			logger.warn("", e);
		}
	
		if (ci != null) {
			ci.initialize(applicationContext);
		}

	}

}

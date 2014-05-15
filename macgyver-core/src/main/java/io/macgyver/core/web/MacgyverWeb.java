package io.macgyver.core.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class MacgyverWeb extends WebMvcConfigurerAdapter {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		logger.info("registering resources");
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"classpath:/web/resources/");
	}
	
	
}

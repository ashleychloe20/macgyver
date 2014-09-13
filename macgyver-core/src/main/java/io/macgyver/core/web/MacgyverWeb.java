package io.macgyver.core.web;

import java.io.File;
import java.net.MalformedURLException;

import io.macgyver.core.Bootstrap;
import io.macgyver.core.MacGyverConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class MacgyverWeb extends WebMvcConfigurerAdapter {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		try {
			String webDir = new File(Bootstrap.getInstance().getWebDir(),
					"resources").getAbsoluteFile().toURI().toURL().toString();
			String classpathLocation = "classpath:/web/resources/";
			logger.info("static resources served from: {} and {}", webDir,
					classpathLocation);
			registry.addResourceHandler("/resources/**").addResourceLocations(
					webDir, classpathLocation);
		} catch (MalformedURLException e) {
			throw new MacGyverConfigurationException(e);
		}
	}

}

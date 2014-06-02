package io.macgyver.core;

import java.io.File;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Simple wrapper to start server.
 * 
 * @author rschoening
 * 
 */

@Configuration
@ComponentScan(basePackages = { "io.macgyver.config",
		"io.macgyver.plugin.config", "io.macgyver.core.config" })
@EnableAutoConfiguration
public class ServerMain {

	static Logger logger = org.slf4j.LoggerFactory.getLogger(ServerMain.class);

	public static String computeTemplateRoots() {
		try {
			File webDir = Bootstrap.getInstance().getVfsManager()
					.getWebLocation();
			String templateRoots = "classpath:/web/templates";

			templateRoots = webDir.toURI().toURL().toString() + ","
					+ templateRoots;

			return templateRoots;
		} catch (MalformedURLException e) {
			throw new ConfigurationException(e);
		}

	}

	public static void main(String[] args) throws Exception {

		{
			// need to move this block upstream to Bootstrap
			System.setProperty("spring.gsp.reloadingEnabled", "true");
			String templateRoots = computeTemplateRoots();
			logger.info("spring.gsp.templateRoots=" + templateRoots);
			System.setProperty("spring.gsp.templateRoots", templateRoots);
		}

		SpringApplication app = new SpringApplication(ServerMain.class);
		app.setShowBanner(false);
		ConfigurableApplicationContext ctx = app.run(args);

		Environment env = ctx.getEnvironment();

		logger.info("Spring environment: {}", env);

	}

}

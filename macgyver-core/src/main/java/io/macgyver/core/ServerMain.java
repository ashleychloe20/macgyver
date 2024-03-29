package io.macgyver.core;

import io.macgyver.core.eventbus.MacGyverEventBus;

import java.io.File;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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



	public static void main(String[] args) throws Exception {

	

		Bootstrap.printBanner();
		
		SpringApplication app = new SpringApplication(ServerMain.class);
		
		
		app.addInitializers(new SpringContextInitializer());
		
		ConfigurableApplicationContext ctx = app.run(args);

		Environment env = ctx.getEnvironment();

		logger.info("spring environment: {}", env);
		Kernel.getInstance().getApplicationContext()
				.getBean(MacGyverEventBus.class)
				.post(new Kernel.ServerStartedEvent(Kernel.getInstance()));
	}

}

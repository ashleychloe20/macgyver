package io.macgyver.core;

import io.macgyver.config.CoreConfig;
import io.macgyver.core.mapdb.BootstrapMapDB;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.collect.Lists;

/**
 * Simple wrapper to start server.
 * 
 * @author rschoening
 * 
 */

@Configuration
@ComponentScan(basePackageClasses = { CoreConfig.class })
@EnableAutoConfiguration
public class ServerMain {

	public static void main(String[] args) throws Exception {

		
		BootstrapMapDB bootstrap = BootstrapMapDB.getInstance();
		bootstrap.init();
		

		
		ConfigurableApplicationContext ctx = SpringApplication.run(ServerMain.class, args);
		Environment env = ctx.getEnvironment();
		
		
		System.out.println(env);
		
	}
}

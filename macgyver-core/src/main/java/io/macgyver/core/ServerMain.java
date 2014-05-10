package io.macgyver.core;

import io.macgyver.config.CoreConfig;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

/**
 * Simple wrapper to start server.
 * @author rschoening
 *
 */

@Configuration
@ComponentScan(basePackageClasses={CoreConfig.class})
@EnableAutoConfiguration
public class ServerMain {

	public static void main(String [] args) throws Exception {
		 SpringApplication.run(ServerMain.class, args);
	}
}

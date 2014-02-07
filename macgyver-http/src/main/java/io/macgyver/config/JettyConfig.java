package io.macgyver.config;

import io.macgyver.http.jetty.JettyServer;
import io.macgyver.http.spark.AdminSparklet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfig {

	int httpPort = 8080;

	@Bean
	public JettyServer jettyServer() {
		return new JettyServer(8080);
		
	}

	@Bean
	public AdminSparklet adminSparklet() {
		return new AdminSparklet();
	}

}

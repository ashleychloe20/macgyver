package io.macgyver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.macgyver.metrics.leftronic.LeftronicClient;
import io.macgyver.metrics.leftronic.LeftronicClientServiceFactory;

@Configuration
public class MetricsConfig {

	
	@Bean(name="leftronicClientServiceFactory")
	public LeftronicClientServiceFactory leftronicClientServiceFactory() {
		return new LeftronicClientServiceFactory();
	}
}

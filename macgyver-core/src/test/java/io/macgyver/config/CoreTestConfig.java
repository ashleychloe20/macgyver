package io.macgyver.config;

import io.macgyver.core.factory.TestBeanServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreTestConfig {
	@Bean
	public TestBeanServiceFactory testBeanServiceFactory() {
		return new TestBeanServiceFactory();
	}
}

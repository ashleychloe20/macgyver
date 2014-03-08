package io.macgyver.config;

import io.macgyver.core.factory.TestBeanServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//@PropertySource("file:./src/test/resources/macgyver-test.properties")
public class CoreTestConfig {

	@Bean
	public TestBeanServiceFactory testBeanServiceFactory() {
		return new TestBeanServiceFactory();
	}
}

package io.macgyver.plugin.config;

import io.macgyver.plugin.elb.a10.A10ClientServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class A10Config{

	@Bean
	public A10ClientServiceFactory macA10ClientServiceFactory() {
		return new A10ClientServiceFactory();
	}
}

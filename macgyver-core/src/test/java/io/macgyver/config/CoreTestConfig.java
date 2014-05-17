package io.macgyver.config;

import io.macgyver.core.factory.TestBeanServiceFactory;
import io.macgyver.core.web.MacgyverWeb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreTestConfig {
	@Bean
	public TestBeanServiceFactory macTestBeanFactory() {
		return new TestBeanServiceFactory();
	}
	
	@Bean
	public MacgyverWeb macWEb() {
		return new MacgyverWeb();
	}
}



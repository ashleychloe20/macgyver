package io.macgyver.plugin.config;

import io.macgyver.plugin.splunk.SplunkServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SplunkConfig {

	@Bean
	public SplunkServiceFactory macNewRelicServiceFactory() {
		return new SplunkServiceFactory();
	}

}

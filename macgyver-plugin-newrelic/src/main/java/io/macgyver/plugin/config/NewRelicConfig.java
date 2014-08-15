package io.macgyver.plugin.config;

import io.macgyver.plugin.newrelic.NewRelicServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NewRelicConfig {

	@Bean
	public NewRelicServiceFactory macNewRelicServiceFactory() {
		return new NewRelicServiceFactory();
	}

}

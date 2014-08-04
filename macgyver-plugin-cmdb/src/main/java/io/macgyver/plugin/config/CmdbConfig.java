package io.macgyver.plugin.config;

import io.macgyver.plugin.cmdb.AppInstanceManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmdbConfig {


	@Bean
	public AppInstanceManager macAppInstanceManager() {
		
		return new AppInstanceManager();
	}
	

}

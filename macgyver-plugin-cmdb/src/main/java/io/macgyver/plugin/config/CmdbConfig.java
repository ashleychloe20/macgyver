package io.macgyver.plugin.config;

import io.macgyver.plugin.cmdb.AppInstanceManager;
import io.macgyver.plugin.cmdb.CmdbApiController;
import io.macgyver.plugin.cmdb.CmdbController;
import io.macgyver.plugin.cmdb.CmdbUIManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmdbConfig {


	@Bean
	public AppInstanceManager macAppInstanceManager() {
		
		return new AppInstanceManager();
	}
	
	@Bean
	public CmdbApiController macCmdbApiController() {
		return new CmdbApiController();
	}
	@Bean
	public CmdbController macCmdbController() {
		return new CmdbController();
	}
	
	@Bean
	public CmdbUIManager macCmdbUIManager() {
		return new CmdbUIManager();
	}
}

package io.macgyver.plugin.config;

import io.macgyver.core.titan.GraphRepository;
import io.macgyver.plugin.cmdb.AppInstanceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thinkaurelius.titan.core.TitanGraph;

@Configuration
public class CmdbConfig {

	@Autowired
	TitanGraph titanGraph;
	
	@Bean
	public AppInstanceManager macAppInstanceManager() {
		
		return new AppInstanceManager(titanGraph);
	}
}

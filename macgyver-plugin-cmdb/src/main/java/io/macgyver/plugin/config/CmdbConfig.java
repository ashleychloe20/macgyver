package io.macgyver.plugin.config;

import io.macgyver.core.titan.GraphRepository;
import io.macgyver.plugin.cmdb.AppInstanceManager;
import io.macgyver.plugin.cmdb.CmdbGraphInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.TransactionalGraph;

@Configuration
public class CmdbConfig {

	@Autowired
	TransactionalGraph graph;
	
	@Bean
	public AppInstanceManager macAppInstanceManager() {
		
		return new AppInstanceManager(graph);
	}
	
	@Bean
	public CmdbGraphInitializer macCmddbTitanConfigurator() {
		return new CmdbGraphInitializer();
	}
}

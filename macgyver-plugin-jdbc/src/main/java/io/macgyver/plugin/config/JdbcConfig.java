package io.macgyver.plugin.config;


import io.macgyver.jdbc.DataSourceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class JdbcConfig {

	@Bean
	public DataSourceFactory dataSourceFactory() {
		return new DataSourceFactory();
	}


}

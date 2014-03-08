package io.macgyver.config;


import io.macgyver.jdbc.DataSourceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jolbox.bonecp.BoneCPDataSource;



@Configuration
public class JdbcConfig {

	@Bean
	public DataSourceFactory dataSourceFactory() {
		return new DataSourceFactory();
	}


}

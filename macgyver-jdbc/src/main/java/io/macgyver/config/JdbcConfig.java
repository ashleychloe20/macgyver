package io.macgyver.config;

import io.macgyver.jdbc.DataSourceSparklet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class JdbcConfig {


	@Bean(name = "jdbcSpark")
	public DataSourceSparklet jdbcSpark() {
		return new DataSourceSparklet();
	}
}

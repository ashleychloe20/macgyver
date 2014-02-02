package io.macgyver.config;

import io.macgyver.mongodb.MongoClientServiceFactory;
import io.macgyver.mongodb.MongoDBServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDBConfig {

	
	


	@Bean
	public MongoDBServiceFactory mongoDBServiceFactory() {
		return new MongoDBServiceFactory();
	}

	@Bean
	public MongoClientServiceFactory mongoClientServiceFactory() {
		return new MongoClientServiceFactory();
	}
}

package io.macgyver.plugin.config;

import io.macgyver.mongodb.MongoDBServiceFactory;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { MongoDBServiceFactory.class })
public class MongoDBConfig {

}

package io.macgyver.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration

@ComponentScan(basePackages={"io.macgyver.config","io.macgyver.plugin.config","io.macgyver.core.config"})
public class TestConfig {

	

}

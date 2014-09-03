package io.macgyver.plugin.config;

import io.macgyver.plugin.cloud.ComputeServiceContextFactory;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses={ComputeServiceContextFactory.class})
public class JCloudsConfig {

}

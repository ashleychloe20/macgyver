package io.macgyver.plugin.config;

import io.macgyver.plugin.cloud.vsphere.VSphereFactory;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses={VSphereFactory.class})
public class VSphereConfig {

}

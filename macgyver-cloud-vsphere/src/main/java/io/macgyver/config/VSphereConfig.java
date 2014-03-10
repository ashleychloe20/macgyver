package io.macgyver.config;

import io.macgyver.cloud.vsphere.VSphereFactory;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses={VSphereFactory.class})
public class VSphereConfig {

}

package io.macgyver.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={"io.macgyver.test.config","io.macgyver.config"})
public class TestConfig {

}

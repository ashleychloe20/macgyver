package io.macgyver.test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This class must not be included/imported by the core runtime.
 * @author rschoening
 *
 */

@Configuration
@ComponentScan(basePackages={"io.macgyver.config","io.macgyver.plugin.config","io.macgyver.core.config"})
public class CoreIntegrationTestConfig {



	
	
}

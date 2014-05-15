package io.macgyver.core.testconfig;

import io.macgyver.config.CoreConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This class must not be included/imported by the core runtime.
 * @author rschoening
 *
 */

@Configuration
@ComponentScan(basePackageClasses={CoreConfig.class})
public class CoreIntegrationTestConfig {



	
	
}

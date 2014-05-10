package io.macgyver.core.testconfig;

import io.macgyver.config.CoreConfig;
import groovy.lang.GroovyShell;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This class must not be included/imported by the core runtime.
 * @author rschoening
 *
 */

@Configuration
@ComponentScan(basePackageClasses={CoreConfig.class})
public class CoreIntegrationTestConfig {



	
	
}

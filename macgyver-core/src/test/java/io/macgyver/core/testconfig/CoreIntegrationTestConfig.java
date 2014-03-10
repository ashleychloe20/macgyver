package io.macgyver.core.testconfig;

import groovy.lang.GroovyShell;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class must not be included/imported by the core runtime.
 * @author rschoening
 *
 */

@Configuration
public class CoreIntegrationTestConfig {



	@Bean
	public static BeanFactoryPostProcessor bff() {
		BeanFactoryPostProcessor pp = new BeanFactoryPostProcessor() {
			
			@Override
			public void postProcessBeanFactory(
					ConfigurableListableBeanFactory beanFactory) throws BeansException {
					GroovyShell gs = new GroovyShell();
					ApplicationContext ctx  = (ApplicationContext) gs.evaluate("import io.macgyver.core.Kernel\nKernel.getInstance().getApplicationContext()");
					beanFactory.setParentBeanFactory(ctx);
				
			}
		};
		
		return pp;
	}
}

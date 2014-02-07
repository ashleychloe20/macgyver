package io.macgyver.test;

import groovy.lang.GroovyShell;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={})
public class TestConfig {

	
	@Bean
	public static BeanFactoryPostProcessor bff() {
		BeanFactoryPostProcessor pp = new BeanFactoryPostProcessor() {
			
			@Override
			public void postProcessBeanFactory(
					ConfigurableListableBeanFactory beanFactory) throws BeansException {
					GroovyShell gs = new GroovyShell();
					ApplicationContext ctx  = (ApplicationContext) gs.evaluate("io.macgyver.core.Kernel.getInstance().getApplicationContext()");
					beanFactory.setParentBeanFactory(ctx);
				
			}
		};
		
		return pp;
	}
}

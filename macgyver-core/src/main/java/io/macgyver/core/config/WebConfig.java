package io.macgyver.core.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import io.macgyver.core.titan.TitanFilter;
import io.macgyver.core.web.CoreApiController;
import io.macgyver.core.web.HomeController;
import io.macgyver.core.web.MacgyverWeb;
import io.macgyver.core.web.navigation.MenuManager;
import io.macgyver.core.web.navigation.StandardMenuDecorator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ComponentScan(basePackageClasses = { HomeController.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WebConfig implements EnvironmentAware {

	

	@Autowired
	private final org.springframework.core.io.ResourceLoader resourceLoader = new DefaultResourceLoader();

	@Override
	public void setEnvironment(Environment environment) {

	}

	@Bean
	public CoreApiController macCoreApiController() {
		return new CoreApiController();
	}

	@Bean
	public BeanPostProcessor macHandlerMappingPostProcessor() {
		return new BeanPostProcessor() {

			@Override
			public Object postProcessBeforeInitialization(Object bean,
					String beanName) throws BeansException {
				if (bean instanceof RequestMappingHandlerMapping
						&& "requestMappingHandlerMapping".equals(beanName)) {
					RequestMappingHandlerMapping m = ((RequestMappingHandlerMapping) bean);
					
					
					
				}

				return bean;
			}

			@Override
			public Object postProcessAfterInitialization(Object bean,
					String beanName) throws BeansException {
				return bean;
			}
		};
	}

	@Bean
	public MacgyverWeb macWebConfig() {
		return new MacgyverWeb();
	}

	@Bean
	public MenuManager macMenuManager() {
		return new MenuManager();
	}

	@Bean
	public StandardMenuDecorator macStandardMenuDecorator() {
		return new StandardMenuDecorator();
	}
	@Bean
	public TitanFilter macTitanFilter() {
		return new TitanFilter();
	}
	@Bean
	public FilterRegistrationBean macTitanFilterReg() {
		FilterRegistrationBean b = new FilterRegistrationBean();
		
		b.setFilter(macTitanFilter());
		return b;
	}
}

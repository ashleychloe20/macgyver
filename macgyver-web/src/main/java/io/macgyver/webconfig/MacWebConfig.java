package io.macgyver.webconfig;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverPropertySourcesPlaceholderConfigurer;
import io.macgyver.web.jetty.StaticResourceServlet;
import io.macgyver.web.rythm.RythmViewResolver;
import io.macgyver.web.rythm.TestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = { MacWebConfig.class })
public class MacWebConfig extends WebMvcConfigurerAdapter {

	Logger log = LoggerFactory.getLogger(MacWebConfig.class);

	@Bean
	public TestController testController() {
		return new TestController();
	}

	@Bean
	public RythmViewResolver rythmViewResolver() {
		return new RythmViewResolver();
	}

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		log.info("configureDefaultServletHandling() method invoked");
		configurer.enable(StaticResourceServlet.class.getName());
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer webSlavePropertySourcesPlaceholderConfigurer() {
		/*
		 * This is required so that @Value annotations work properly in the child
		 * WebApplicationContext.  Our main PropertySourcesPlaceholderConfigurer returns a "slave" copy
		 * with the same effective properties.  It might be possible to use the same PropertySourcesPlaceholderConfigurer
		 * in the child context, but who knows if this is supported.  
		 */
		PropertySourcesPlaceholderConfigurer ppc = Kernel.getInstance().getApplicationContext()
				.getBean(MacGyverPropertySourcesPlaceholderConfigurer.class)
				.getSlavePropertySourcesPlaceholderConfigurer();
		
		return ppc;

	}
}

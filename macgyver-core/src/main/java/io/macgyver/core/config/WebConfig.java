package io.macgyver.core.config;


import io.macgyver.core.web.CoreApiController;
import io.macgyver.core.web.HomeController;
import io.macgyver.core.web.MacgyverWeb;
import io.macgyver.core.web.navigation.MenuManager;
import io.macgyver.core.web.navigation.StandardMenuDecorator;
import io.macgyver.core.web.rythm.MacGyverRythmResourceLoader;
import io.macgyver.core.web.rythm.RythmViewResolver;

import java.util.Map;

import org.rythmengine.RythmEngine;
import org.rythmengine.conf.RythmConfigurationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;

import com.google.common.collect.Maps;

@Configuration
@ComponentScan(basePackageClasses={HomeController.class})
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class WebConfig implements EnvironmentAware {

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";

	@Autowired
	private final org.springframework.core.io.ResourceLoader resourceLoader = new DefaultResourceLoader();


	@Override
	public void setEnvironment(Environment environment) {

	}

	@Bean
	public CoreApiController coreApiController() {
		return new CoreApiController();
	}
	
	






	
	@Bean
	public MacgyverWeb macgyverWebConfig() {
		return new MacgyverWeb();
	}
	@Bean
	public RythmViewResolver rythmViewResolver() {
		return new RythmViewResolver();
	}
	
	@Bean(name="io.macgyver.web.RythmEngine")
	public RythmEngine macgyverRythmEngine() {
		Map<String,String> cfg = Maps.newHashMap();	
		cfg.put(RythmConfigurationKey.RESOURCE_LOADER_IMPLS.getKey(), MacGyverRythmResourceLoader.class.getName());
		cfg.put(RythmConfigurationKey.ENGINE_MODE.getKey(), "dev");
		RythmEngine re = new RythmEngine(cfg);
		
		MacGyverRythmResourceLoader.setRhythmEngine(re);
		return re;
	}

	
	@Bean
	public MenuManager menuManager() {
		return new MenuManager();
	}
	
	@Bean
	public StandardMenuDecorator standardMenuDecorator() {
		return new StandardMenuDecorator();
	}
}

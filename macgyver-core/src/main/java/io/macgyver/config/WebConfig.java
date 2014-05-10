package io.macgyver.config;


import io.macgyver.core.web.HomeController;
import io.macgyver.core.web.MacgyverWeb;
import io.macgyver.core.web.navigation.MenuManager;
import io.macgyver.core.web.navigation.StandardMenuDecorator;
import io.macgyver.core.web.rythm.MacGyverRythmResourceLoader;
import io.macgyver.core.web.rythm.RythmViewResolver;
import io.macgyver.core.web.shiro.DelegatingAuthorizingRealm;
import io.macgyver.core.web.shiro.MacGyverFilter;
import io.macgyver.core.web.shiro.StaticAuthorizingRealm;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.rythmengine.RythmEngine;
import org.rythmengine.conf.RythmConfigurationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.google.common.collect.Lists;
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


	
	
	@Bean(name = "defaultWebSecurityManager")
	public DefaultWebSecurityManager defaultWebSecurityManager() {
		DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();

		dwsm.setRealm(delegatingAuthorizingRealm());
		return dwsm;
	}

	@Bean(name = "shiroFilter")
	@DependsOn(value = "defaultWebSecurityManager")
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean sf = new ShiroFilterFactoryBean();
		sf.setSecurityManager(defaultWebSecurityManager());
		sf.setFilterChainDefinitions("/api/**= anon\n/resources/** = anon\n/webjars/**=anon\n/auth/logout = logout\n/**=authc");
		sf.setLoginUrl("/auth/login");
		sf.setSuccessUrl("/");
		return sf;
	}

	@Bean
	public DelegatingAuthorizingRealm delegatingAuthorizingRealm() {
		return new DelegatingAuthorizingRealm();
	}

	@Bean
	public StaticAuthorizingRealm staticAuthorizingRealm() {
		return new StaticAuthorizingRealm();
	}

	@Bean
	public FilterRegistrationBean shiroFilterProxy() {
		FilterRegistrationBean b = new FilterRegistrationBean();
		b.setFilter(new DelegatingFilterProxy());
		b.setName("shiroFilter");
		Map<String,String> m = Maps.newConcurrentMap();
		m.put("targetFilterLifecycle", "true");
		b.setInitParameters(m);
		b.setUrlPatterns(Lists.newArrayList("/*"));
		
		return b;
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
	public MacGyverFilter macgyverFilter() {
		return new MacGyverFilter();
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

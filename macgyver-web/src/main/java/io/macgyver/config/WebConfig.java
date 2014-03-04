package io.macgyver.config;

import io.macgyver.http.jetty.JettyServer;
import io.macgyver.http.shiro.DelegatingAuthorizingRealm;
import io.macgyver.http.shiro.StaticAuthorizingRealm;
import io.macgyver.web.rythm.MacGyverRythmResourceLoader;

import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.eclipse.jetty.http.MimeTypes;
import org.rythmengine.RythmEngine;
import org.rythmengine.conf.RythmConfigurationKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.google.common.collect.Maps;


@Configuration
public class WebConfig {

	Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Value("${macgyver.web.http.port:8080}")
	int httpPort;

	@Bean
	public JettyServer jettyServer() {
		logger.info("macgyver.web.http.port: {}", httpPort);
		return new JettyServer(httpPort);
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

	@Bean(name="io.macgyver.web.MimeTypes")
	public MimeTypes macgyverWebMimeTypes() {
		return new MimeTypes();
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

}

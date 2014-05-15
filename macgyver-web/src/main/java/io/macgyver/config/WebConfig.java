package io.macgyver.config;

import io.macgyver.core.web.rythm.MacGyverRythmResourceLoader;
import io.macgyver.web.jetty.JettyServer;
import io.macgyver.web.shiro.DelegatingAuthorizingRealm;
import io.macgyver.web.shiro.StaticAuthorizingRealm;

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
	
	


}

package io.macgyver.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.macgyver.core.ConfigurationException;
import io.macgyver.http.jetty.JettyServer;
import io.macgyver.http.shiro.DelegatingAuthorizingRealm;
import io.macgyver.http.shiro.StaticAuthorizingRealm;
import io.macgyver.http.spark.AuthSparklet;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.eclipse.jetty.http.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

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

	@Bean
	public AuthSparklet adminSparklet() {
		return new AuthSparklet();
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

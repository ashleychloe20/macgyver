package io.macgyver.config;

import io.macgyver.ldap.LdapFactoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LdapConfig {

	@Bean
	public LdapFactoryBean ldapServiceFactory() {
		return new LdapFactoryBean("ldap");
	}

	@Bean
	public LdapFactoryBean activeDirectoryServiceFactory() {
		return new LdapFactoryBean("activedirectory");
	}
}

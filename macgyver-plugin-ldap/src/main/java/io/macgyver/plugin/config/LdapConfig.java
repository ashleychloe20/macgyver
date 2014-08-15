package io.macgyver.plugin.config;

import io.macgyver.ldap.LdapServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LdapConfig {

	@Bean
	public LdapServiceFactory ldapServiceFactory() {
		return new LdapServiceFactory("ldap");
	}

	@Bean
	public LdapServiceFactory activeDirectoryServiceFactory() {
		return new LdapServiceFactory("activedirectory");
	}
}

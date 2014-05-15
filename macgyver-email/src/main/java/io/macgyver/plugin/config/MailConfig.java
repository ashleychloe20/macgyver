package io.macgyver.plugin.config;

import io.macgyver.email.SmtpServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

	@Bean
	public SmtpServiceFactory smtpServiceFactory() {
		return new SmtpServiceFactory();
	}
}

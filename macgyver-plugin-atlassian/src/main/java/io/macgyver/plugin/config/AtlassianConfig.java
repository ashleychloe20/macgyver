package io.macgyver.plugin.config;

import org.springframework.context.annotation.Bean;

import io.macgyver.plugin.atlassian.jira.JiraServiceFactory;

public class AtlassianConfig {

	
	@Bean
	JiraServiceFactory jiraServiceFactory() {
		return new JiraServiceFactory();
	}
}

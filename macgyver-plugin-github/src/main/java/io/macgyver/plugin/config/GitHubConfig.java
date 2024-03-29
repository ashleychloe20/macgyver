package io.macgyver.plugin.config;

import io.macgyver.plugin.github.GitHubServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubConfig {

	@Bean
	public GitHubServiceFactory githubServiceFactory() {
		return new GitHubServiceFactory();
	}
	
}

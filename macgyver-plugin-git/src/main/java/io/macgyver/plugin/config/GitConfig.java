package io.macgyver.plugin.config;

import io.macgyver.plugin.git.GitRepositoryServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitConfig {

	@Bean
	public GitRepositoryServiceFactory macGitRepositoryServiceFactory() {
		return new GitRepositoryServiceFactory();
	}

}

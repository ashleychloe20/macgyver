package io.macgyver.config;

import io.macgyver.github.GitHubServiceFactory;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GitHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.AllowConcurrentEvents;

@Configuration
public class GitHubConfig {

	@Bean
	public GitHubServiceFactory githubServiceFactory() {
		return new GitHubServiceFactory();
	}
	
}

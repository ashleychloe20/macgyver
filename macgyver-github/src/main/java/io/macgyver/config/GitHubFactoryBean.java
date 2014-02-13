package io.macgyver.config;

import io.macgyver.core.ServiceFactoryBean;

import java.io.IOException;

import org.kohsuke.github.GitHub;

public class GitHubFactoryBean extends ServiceFactoryBean<GitHub> {

	public GitHubFactoryBean() {
		super(GitHub.class);
	}

	
	@Override
	public GitHub createObject () {
		try {
			String url = getProperties().getProperty("url");
			String oauthToken = getProperties().getProperty("oauthToken");
			GitHub gh = null;
			if (url != null) {
				gh = GitHub.connectToEnterprise(url, oauthToken);
			} else {
				gh = GitHub.connectUsingOAuth(oauthToken);

			}
			return gh;
		} catch (IOException e) {
			throw new io.macgyver.core.ConfigurationException(
					"problem creating GitHub client", e);
		}
	}

}

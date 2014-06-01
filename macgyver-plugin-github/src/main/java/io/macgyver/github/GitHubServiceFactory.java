package io.macgyver.github;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.io.IOException;
import java.util.Properties;

import org.kohsuke.github.GitHub;

public class GitHubServiceFactory extends BasicServiceFactory<GitHub> {

	public GitHubServiceFactory() {
		super("github");
	}

	@Override
	protected GitHub doCreateInstance(ServiceDefinition def) {
		try {
			Properties props = def.getProperties();
			String url = props.getProperty("url");
			String oauthToken = props.getProperty("oauthToken");
			GitHub gh = null;
			if (url != null) {
				logger.info("connecting to: {}", url);
				gh = GitHub.connectToEnterprise(url, oauthToken);
			} else {
				logger.info("connecting to github.com");
				gh = GitHub.connectUsingOAuth(oauthToken);

			}
			return gh;
		} catch (IOException e) {
			throw new io.macgyver.core.ConfigurationException(
					"problem creating GitHub client", e);
		}
	}

}

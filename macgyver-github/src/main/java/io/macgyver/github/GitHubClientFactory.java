package io.macgyver.github;

import io.macgyver.core.ServiceFactory;

import java.io.IOException;

import org.kohsuke.github.GitHub;

public class GitHubClientFactory implements ServiceFactory<GitHub> {

	public GitHubClientFactory() {

	}

	String url;
	String oauthToken;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOauthToken() {
		return oauthToken;
	}

	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}

	@Override
	public GitHub get() {
		try {
			GitHub gh = null;
			if (url != null) {
				gh = GitHub.connectToEnterprise(url, oauthToken);
			} else {
				gh = GitHub.connectUsingOAuth(oauthToken);

			}
			return gh;
		} catch (IOException e) {
			throw new io.macgyver.core.ConfigurationException("problem creating GitHub client", e);
		}
	}

}

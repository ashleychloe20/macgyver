package io.macgyver.github;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.ServiceFactoryBean;

import java.io.IOException;
import java.util.Properties;

import javax.json.JsonObject;

import org.kohsuke.github.GitHub;

public class GitHubClientFactory extends ServiceFactoryBean<GitHub> {

	public GitHubClientFactory() {
		super(GitHub.class);
	
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
	public GitHub getObject() throws Exception {
		GitHub gh = null;
		if (url != null) {
			gh = GitHub.connectToEnterprise(url, oauthToken);
		} else {
			gh = GitHub.connectUsingOAuth(oauthToken);

		}
		return gh;
	}

	
}

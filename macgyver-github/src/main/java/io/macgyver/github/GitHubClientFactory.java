package io.macgyver.github;

import groovy.util.ConfigObject;
import io.macgyver.core.MultiToolException;
import io.macgyver.core.ServiceFactory;

import java.io.IOException;
import java.util.Properties;

import javax.json.JsonObject;

import org.kohsuke.github.GitHub;

import com.google.common.base.Optional;

public class GitHubClientFactory extends ServiceFactory<GitHub> {

	public GitHubClientFactory() {
		super("github");
	
	}

	public GitHub create(String name, JsonObject cfg) {

		try {
	
			Properties props = toProperties(cfg);
			
			String url = props.getProperty("url");
			String oauthToken = props.getProperty("oauthToken");
			GitHub gh = null;
			if (url != null) {
				gh = GitHub.connectToEnterprise(url, oauthToken);
			} else {
				gh = GitHub.connectUsingOAuth(oauthToken);

			}
			return gh;
		} catch (IOException e) {
			throw new MultiToolException(e);
		}

	}

	
}

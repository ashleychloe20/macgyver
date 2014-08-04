package io.macgyver.github.resource.provider;

import io.macgyver.core.resource.Resource;
import io.macgyver.plugin.github.resource.provider.GitHubResourceProvider;
import io.macgyver.test.MacGyverIntegrationTest;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class GitHubResourceProviderTest extends MacGyverIntegrationTest{



	@Test
	@Ignore
	public void testX() throws IOException {
		
		GitHub gh = GitHub.connectAnonymously();
		
		GHRepository repo = gh.getRepository("if6was9/macgyver-resource-test");
		
		
	//	GitHub gh = GitHub.connectToEnterprise(getPrivateProperty("github.api.url"), getPrivateProperty("github.api.oauthToken"));
		
	
		
		
		GitHubResourceProvider p = new GitHubResourceProvider(gh);
		p.setRepoName("if6was9/macgyver-resource-test");
		p.setRef("refs/heads/master");
		
		for (Resource r:  p.findResources()) {
			logger.debug("{}",r.getContentAsString());
			logger.debug("{}",r);
		}
		
	}
}

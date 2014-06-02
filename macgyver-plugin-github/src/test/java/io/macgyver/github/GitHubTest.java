package io.macgyver.github;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacGyverIntegrationTest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRef;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;

public class GitHubTest extends MacGyverIntegrationTest {

	@Autowired
	ServiceRegistry reg;

	@Autowired
	GitHubServiceFactory githubServiceFactory;
	
	
	@Test
	public void testX() throws IOException {
		GitHub gh = (GitHub) reg.get("testGitHub");

		Assert.assertNotNull(gh);
		
		Assert.assertNotNull(githubServiceFactory);
	}
	
	@Test
	public void testY() throws IOException {
	
		GitHub gh = GitHub.connectToEnterprise(getPrivateProperty("github.api.url"), getPrivateProperty("github.api.oauthToken"));
		
		
		Assert.assertNotNull(gh);
		
		GHOrganization org = gh.getOrganization("OPS");
		
	/*
		
		for (GHRepository repo: org.listRepositories()) {
			System.out.println(repo);
		}
		*/
		
	
		GHRepository x2 = gh.getRepository("OPS/macgyver-toolbox");
		
		System.out.println(x2);
	
		for (GHContent c: x2.getDirectoryContent("scripts/scheduler","refs/heads/master")) {
			System.out.println(c.getPath());
			System.out.println(c.isDirectory());
			System.out.println(c.getSha());

		}
		
	}

}

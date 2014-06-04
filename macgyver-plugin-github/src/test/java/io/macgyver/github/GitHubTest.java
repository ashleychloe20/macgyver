package io.macgyver.github;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.plugin.github.GitHubServiceFactory;
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
	


}

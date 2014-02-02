package io.macgyver.github;

import io.macgyver.core.Kernel;
import io.macgyver.core.ServiceNotFoundException;
import io.macgyver.test.MacgyverIntegrationTest;

import java.io.IOException;

import org.junit.Test;
import org.kohsuke.github.GitHub;

public class GitHubTest extends MacgyverIntegrationTest {

	
	@Test(expected=ServiceNotFoundException.class)
	public void testGitHubInvalidService() throws IOException {
		
		
		GitHubClientFactory cf = Kernel.getInstance().getApplicationContext().getBean(GitHubClientFactory.class);
		GitHub gh = cf.get("INVALID");
		
		
	}
}

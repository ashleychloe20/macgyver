package io.macgyver.github;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacgyverIntegrationTest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;

public class GitHubTest extends MacgyverIntegrationTest {

	@Autowired
	ServiceRegistry reg;

	@Test
	public void testX() throws IOException {
		GitHub gh = (GitHub) reg.get("testGitHub");

		Assert.assertNotNull(gh);
	}

}

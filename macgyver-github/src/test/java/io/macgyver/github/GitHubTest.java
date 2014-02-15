package io.macgyver.github;

import io.macgyver.core.ServiceFactoryClassFinder;
import io.macgyver.test.MacgyverIntegrationTest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.kohsuke.github.GitHub;

public class GitHubTest extends MacgyverIntegrationTest {

	@Test
	public void testX() throws IOException {
		GitHub gh = applicationContext.getBean("testGitHub",GitHub.class);
		Assert.assertNotNull(gh);
	}

	@Test
	public void serviceLocatorTest() throws ClassNotFoundException {
		ServiceFactoryClassFinder locator = new ServiceFactoryClassFinder();

		Assert.assertEquals(GitHubFactoryBean.class,
				locator.forServiceType("github"));
	}
}

package io.macgyver.plugin.atlassian.jira;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Test;

public class JiraClientImplTest {

	@Test
	public void testNullArg() {

		try {
			new JiraClientImpl(null, "abc", "def");
			Assert.fail();
		} catch (NullPointerException e) {
			assertThat(e).isInstanceOf(NullPointerException.class);
		}
		

	}
	
	@Test
	public void testInvalidProtocol() {

		try {
			new JiraClientImpl("ftp://example.com", "abc", "def");
			Assert.fail();
		} catch (Exception e) {
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
		}
	}

}

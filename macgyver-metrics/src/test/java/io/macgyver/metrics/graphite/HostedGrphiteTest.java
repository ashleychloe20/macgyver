package io.macgyver.metrics.graphite;

import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;

public class HostedGrphiteTest extends MacgyverIntegrationTest{

	@Test
	public void testHostedGraphite() throws Exception {
		HostedGraphite g = applicationContext.getBean("testHostedGraphite",
				HostedGraphite.class);
		g.record("macgyver.abc", 123);
		Assert.assertNotNull(g);

		Thread.sleep(2000);
	}
}

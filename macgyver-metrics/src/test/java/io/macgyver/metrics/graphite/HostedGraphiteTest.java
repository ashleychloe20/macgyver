package io.macgyver.metrics.graphite;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HostedGraphiteTest extends MacgyverIntegrationTest {

	@Autowired
	ServiceRegistry registry;

	@Test
	public void testHostedGraphite() throws Exception {
		HostedGraphite g = (HostedGraphite) registry.get("testHostedGraphite");

		g.record("macgyver.abc", 123);
		Assert.assertNotNull(g);

		Thread.sleep(2000);
	}

	@Test
	public void testQuery() throws Exception {

		// http://graphite.readthedocs.org/en/latest/render_api.html

	}
}

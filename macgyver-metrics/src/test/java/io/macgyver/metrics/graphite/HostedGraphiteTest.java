package io.macgyver.metrics.graphite;

import java.util.Map;

import io.macgyver.core.factory.ServiceInstanceRegistry;
import io.macgyver.metrics.TSV;
import io.macgyver.test.MacgyverIntegrationTest;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class HostedGraphiteTest extends MacgyverIntegrationTest{

	@Autowired
	ClientConfig clientConfig;
	
	@Autowired
	ServiceInstanceRegistry registry;
	
	@Test
	public void testHostedGraphite() throws Exception {
		HostedGraphite g = (HostedGraphite) registry.get("testHostedGraphite");
		
	
		g.record("macgyver.abc", 123);
		Assert.assertNotNull(g);

		Thread.sleep(2000);
	}
	
	@Test
	public void testQuery()  throws Exception {
		
		// http://graphite.readthedocs.org/en/latest/render_api.html
		

	}
}

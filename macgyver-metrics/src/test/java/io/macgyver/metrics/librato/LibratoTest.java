package io.macgyver.metrics.librato;

import io.macgyver.core.factory.ServiceInstanceRegistry;
import io.macgyver.metrics.graphite.HostedGraphite;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LibratoTest extends MacgyverIntegrationTest{

	@Autowired
	ServiceInstanceRegistry registry;
	
	@Test
	public void testConfig() throws Exception {
		Librato librato = registry.get("testLibrato", Librato.class);
		librato.record("macgyver abc", 123);
		Assert.assertNotNull(librato);
		
		Thread.sleep(2000);
	}
	
	

}

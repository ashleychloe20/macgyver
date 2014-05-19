package io.macgyver.metrics.librato;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LibratoTest extends MacIntegrationTest{

	@Autowired
	ServiceRegistry registry;
	
	@Test
	public void testConfig() throws Exception {
		Librato librato = registry.get("testLibrato", Librato.class);
		librato.record("macgyver abc", 123);
		Assert.assertNotNull(librato);
		
		Thread.sleep(2000);
	}
	
	

}

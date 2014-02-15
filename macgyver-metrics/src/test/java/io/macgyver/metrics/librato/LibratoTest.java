package io.macgyver.metrics.librato;

import io.macgyver.metrics.graphite.HostedGraphite;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;

public class LibratoTest extends MacgyverIntegrationTest{

	
	@Test
	public void testConfig() throws Exception {
		Librato librato = applicationContext.getBean("testLibrato", Librato.class);
		librato.record("macgyver abc", 123);
		Assert.assertNotNull(librato);
		
		Thread.sleep(2000);
	}
	
	

}

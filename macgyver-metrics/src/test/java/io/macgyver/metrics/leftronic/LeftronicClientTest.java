package io.macgyver.metrics.leftronic;

import io.macgyver.core.factory.ServiceInstanceRegistry;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LeftronicClientTest extends MacgyverIntegrationTest {

	@Autowired
	ServiceInstanceRegistry registry; 
	
	@Test
	public void testX() throws Exception {

		Leftronic leftronic = registry.get("testLeftronic",
				Leftronic.class);

		Assert.assertNotNull(leftronic);
	}
}

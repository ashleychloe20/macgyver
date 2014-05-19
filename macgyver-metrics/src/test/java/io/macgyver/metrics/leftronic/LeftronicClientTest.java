package io.macgyver.metrics.leftronic;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LeftronicClientTest extends MacIntegrationTest {

	@Autowired
	ServiceRegistry registry; 
	
	@Test
	public void testX() throws Exception {

		Leftronic leftronic = registry.get("testLeftronic",
				Leftronic.class);

		Assert.assertNotNull(leftronic);
	}
}

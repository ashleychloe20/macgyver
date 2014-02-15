package io.macgyver.metrics.leftronic;

import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class LeftronicClientTest extends MacgyverIntegrationTest {

	@Test
	public void testX() throws Exception {

		Leftronic leftronic = applicationContext.getBean("testLeftronic",
				Leftronic.class);

		Assert.assertNotNull(leftronic);
	}
}

package io.macgyver.test;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class MacgyverIntegrationTestTest extends MacgyverIntegrationTest {
	
	@Autowired
	ApplicationContext ctx;
	
	@org.junit.Test
	public void testIt() {
		Assert.assertNotNull(ctx);
	}
}
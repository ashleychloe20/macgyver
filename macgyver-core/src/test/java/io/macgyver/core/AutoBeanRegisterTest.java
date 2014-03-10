package io.macgyver.core;

import io.macgyver.core.service.ServiceRegistry;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoBeanRegisterTest extends CoreIntegrationTestCase {

	
	@Autowired
	ServiceRegistry registry;
	
	
	@Test
	public void testRegistration() {
		
		Assert.assertNotNull(registry);
	}
}

package io.macgyver.core;

import io.macgyver.core.service.ServiceInstanceRegistry;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoBeanRegisterTest extends CoreIntegrationTestCase {

	
	@Autowired
	ServiceInstanceRegistry registry;
	
	
	@Test
	public void testRegistration() {
		
		Assert.assertNotNull(registry);
	}
}

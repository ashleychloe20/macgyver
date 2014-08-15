package io.macgyver.core;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class AutoBeanRegisterTest extends MacGyverIntegrationTest {

	
	@Autowired
	ServiceRegistry registry;
	
	
	@Test
	public void testRegistration() {
		
		Assert.assertNotNull(registry);
	}
}

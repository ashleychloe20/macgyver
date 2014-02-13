package io.macgyver.core;

import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class AutoBeanRegisterTest extends MacgyverIntegrationTest {

	
	@Autowired
	ApplicationContext applicationContext;
	
	
	@Test
	public void testRegistration() {
		Assert.assertNotNull(applicationContext.getBean("testBeanName"));
		
	}
}

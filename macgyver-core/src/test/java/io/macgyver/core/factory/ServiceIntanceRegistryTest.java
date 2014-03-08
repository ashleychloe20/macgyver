package io.macgyver.core.factory;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.macgyver.core.ServiceNotFoundException;
import io.macgyver.test.MacgyverIntegrationTest;

public class ServiceIntanceRegistryTest extends MacgyverIntegrationTest{

	@Autowired
	ServiceInstanceRegistry reg;
	
	
	@Test
	public void testX() {
		Assert.assertNotNull(reg.get("unittest.testBeanName"));
		
		Assert.assertSame(reg.get("unittest.testBeanName"),reg.get("unittest.testBeanName"));
	}
	
	@Test(expected=ServiceNotFoundException.class)
	public void testNotFound() {
		reg.get("not_found");
	}
}

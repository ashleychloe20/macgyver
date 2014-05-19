package io.macgyver.core.factory;

import io.macgyver.core.ServiceNotFoundException;
import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceIntanceRegistryTest extends MacGyverIntegrationTest {

	@Autowired
	ServiceRegistry reg;

	@Test
	public void testX() {
		logger.info("testing ServiceRegistry: {}",reg);
		Assert.assertNotNull(reg.get("unittest.testBeanName"));

		Assert.assertSame(reg.get("unittest.testBeanName"),
				reg.get("unittest.testBeanName"));
	}

	@Test(expected = ServiceNotFoundException.class)
	public void testNotFound() {
		reg.get("not_found");
	}
}

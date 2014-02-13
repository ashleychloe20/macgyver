package io.macgyver.jdbc;

import io.macgyver.core.Kernel;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class DataSourceFactoryTest extends MacgyverIntegrationTest {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	Kernel kernel;
	
	@Test
	public void testApplicationContextIntegrity() {
		Assert.assertNotNull(applicationContext);
		Assert.assertSame(Kernel.getInstance(), kernel);
		Assert.assertSame(Kernel.getInstance(),applicationContext.getBean("macgyverKernel"));

	}

}

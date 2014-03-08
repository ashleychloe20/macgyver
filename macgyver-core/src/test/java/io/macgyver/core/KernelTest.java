package io.macgyver.core;

import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;


public class KernelTest extends CoreIntegrationTestCase {

	@Autowired
	ApplicationContext applicationContext;
	
	@Value("${SOME_TEST_PROPERTY}")
	String xxx;
	
	@Test
	public void testKernel() {
		Kernel lm = Kernel.getInstance();
		Assert.assertNotNull(lm);
		
		Assert.assertSame(lm, applicationContext.getBean(Kernel.class));
	}
	

}

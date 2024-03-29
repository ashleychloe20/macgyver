package io.macgyver.core;

import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

public class KernelTest extends MacGyverIntegrationTest {

	@Autowired
	ApplicationContext applicationContext;

	@Value("${SOME_TEST_PROPERTY:somedefault}")
	String xxx;

	@Autowired
	Kernel wiredKernel;

	@Test
	public void testKernel() {

		Assert.assertNotNull(wiredKernel);
		Kernel lm = Kernel.getInstance();
		Assert.assertNotNull(lm);

		// Assert.assertSame("x"+lm, applicationContext.getBean(Kernel.class));
	}

}

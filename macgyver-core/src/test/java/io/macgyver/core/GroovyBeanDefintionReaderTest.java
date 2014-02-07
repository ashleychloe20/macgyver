package io.macgyver.core;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import io.macgyver.test.MacgyverIntegrationTest;

public class GroovyBeanDefintionReaderTest extends MacgyverIntegrationTest {

	@Autowired
	ApplicationContext ctx;
	
	
	@Test
	public void testIt() {
		Assert.assertNotNull(ctx.getBean("myTestBean"));
		
		Assert.assertEquals("from-macgyver-core",ctx.getBean("myTestBean",TestBean.class).getFoo());
		
	}
}

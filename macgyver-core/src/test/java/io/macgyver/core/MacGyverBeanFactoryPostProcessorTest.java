package io.macgyver.core;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MacGyverBeanFactoryPostProcessorTest extends CoreIntegrationTestCase {

	@Autowired(required=false)
	@Qualifier("testGroovyBean")
	DummyBean testBean;
	
	@Test
	public void testIt() {
		Assert.assertNotNull(testBean);
		Assert.assertEquals("Jerry Garcia",testBean.getFoo());
	}
}

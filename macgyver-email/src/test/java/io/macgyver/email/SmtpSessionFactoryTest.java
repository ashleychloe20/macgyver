package io.macgyver.email;
import io.macgyver.core.service.ServiceInstanceRegistry;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public class SmtpSessionFactoryTest extends MacgyverIntegrationTest{


	@Autowired
	ApplicationContext ctx;
	
	@Autowired ServiceInstanceRegistry registry;
	
	@Test
	public void testSmtpSessionFactory() {
		
		Assert.assertNotNull(registry);
		
	}
	

}

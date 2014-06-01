package io.macgyver.email;
import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public class SmtpSessionFactoryTest extends MacGyverIntegrationTest{


	@Autowired
	ApplicationContext ctx;
	
	@Autowired ServiceRegistry registry;
	
	@Test
	public void testSmtpSessionFactory() {
		
		Assert.assertNotNull(registry);
		
	}
	

}

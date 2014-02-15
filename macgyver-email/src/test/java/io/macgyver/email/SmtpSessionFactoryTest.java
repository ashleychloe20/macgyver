package io.macgyver.email;
import io.macgyver.config.SmtpFactoryBean;
import io.macgyver.test.MacgyverIntegrationTest;

import java.util.Properties;

import javax.mail.Session;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public class SmtpSessionFactoryTest extends MacgyverIntegrationTest{

	@Autowired
	Session session;
	
	@Autowired
	SmtpClient client;
	
	@Autowired
	ApplicationContext ctx;
	
	@Test
	public void testMailAutoRegister() {
		
		Assert.assertNotNull(session);
		
		Assert.assertNotNull(client);
		
		
		Assert.assertNotNull(ctx.getBean("testMailSession"));
		Assert.assertNotNull(ctx.getBean("testMailSessionClient"));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFactoryBeanMissingHost() {
		
		SmtpFactoryBean f = new SmtpFactoryBean();
		
		Session s = f.createObject();
	
		Assert.assertNotNull(s);

	}
	@Test
	public void testFactoryBean() {
		
		SmtpFactoryBean f = new SmtpFactoryBean();
		
		java.util.Properties props = new Properties();
		props.put("host", "smtp.gmail.com");
		f.setProperties(props);
		
		Session s = f.createObject();
	
		Assert.assertNotNull(s);

	}
}

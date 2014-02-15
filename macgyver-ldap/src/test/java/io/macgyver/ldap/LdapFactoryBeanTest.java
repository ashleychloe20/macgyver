package io.macgyver.ldap;

import io.macgyver.core.ServiceFactoryClassFinder;
import io.macgyver.test.MacgyverIntegrationTest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

public class LdapFactoryBeanTest extends MacgyverIntegrationTest {

	
	@Test
	public void testLocator() throws ClassNotFoundException {
		ServiceFactoryClassFinder locator = new ServiceFactoryClassFinder();
		Assert.assertEquals(LdapFactoryBean.class,locator.forServiceType("ldap"));
		Assert.assertEquals(LdapFactoryBean.class,locator.forServiceType("activedirectory"));
	}
	
	@Test
	public void testConfig() {
		LdapContextSource cs = applicationContext.getBean("testLdap",LdapContextSource.class);
		Assert.assertNotNull(cs);
		
		
		LdapTemplate t = applicationContext.getBean("testLdapTemplate",LdapTemplate.class);
		Assert.assertNotNull(t);
		Assert.assertSame(cs,t.getContextSource());
		
	}
}

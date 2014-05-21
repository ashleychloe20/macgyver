package io.macgyver.ldap;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

public class LdapFactoryBeanTest extends MacGyverIntegrationTest {

	@Autowired
	ServiceRegistry registry;

	@Test
	public void testLocator() throws ClassNotFoundException {

		Assert.assertEquals(LdapServiceFactory.class, registry
				.getServiceFactory("ldap").getClass());
		Assert.assertEquals(LdapServiceFactory.class, registry
				.getServiceFactory("activedirectory").getClass());
	}

	public void testConfig() {
		LdapContextSource cs = applicationContext.getBean("testLdap",
				LdapContextSource.class);
		Assert.assertNotNull(cs);

		LdapTemplate t = applicationContext.getBean("testLdapTemplate",
				LdapTemplate.class);
		Assert.assertNotNull(t);
		Assert.assertSame(cs, t.getContextSource());

	}
}

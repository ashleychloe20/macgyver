package io.macgyver.mongodb;

import io.macgyver.core.service.ServiceInstanceRegistry;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MongoDBFactoryBeanTest extends MacgyverIntegrationTest {

	@Autowired
	ServiceInstanceRegistry reg;

	@Test(expected = IllegalArgumentException.class)
	public void testCredentialInjectorIllegalArgument() {
		MongoDBServiceFactory cf = new MongoDBServiceFactory();

		cf.injectCredentials("something", "abc", "def");
	}

	@Test()
	public void testCredentialInjectorNoCredentials() {
		MongoDBServiceFactory cf = new MongoDBServiceFactory();

		Assert.assertEquals("mongodb://host/db",
				cf.injectCredentials("mongodb://host/db", null, null));
		Assert.assertEquals("mongodb://host/db",
				cf.injectCredentials("mongodb://host/db", "", ""));
	}

	@Test()
	public void testCredentialInjectorWIthCredentials() {
		MongoDBServiceFactory cf = new MongoDBServiceFactory();

		Assert.assertEquals("mongodb://scott:tiger@host/db",
				cf.injectCredentials("mongodb://host/db", "scott", "tiger"));

	}

	@Test
	public void testConfig() {
		Assert.assertNotNull(reg.get("testMongo"));

		Assert.assertNotNull(reg.get("testMongoDB"));
	}

}

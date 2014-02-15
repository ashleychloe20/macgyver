package io.macgyver.mongodb;

import java.net.UnknownHostException;

import io.macgyver.core.ServiceFactoryClassFinder;
import io.macgyver.mongodb.MongoDBFactoryBean.ExtendedMongoClient;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBFactoryBeanTest  extends MacgyverIntegrationTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCredentialInjectorIllegalArgument() {
		MongoDBFactoryBean cf = new MongoDBFactoryBean();

		cf.injectCredentials("something", "abc", "def");
	}

	@Test()
	public void testCredentialInjectorNoCredentials() {
		MongoDBFactoryBean cf = new MongoDBFactoryBean();

		Assert.assertEquals("mongodb://host/db",
				cf.injectCredentials("mongodb://host/db", null, null));
		Assert.assertEquals("mongodb://host/db",
				cf.injectCredentials("mongodb://host/db", "", ""));
	}

	@Test()
	public void testCredentialInjectorWIthCredentials() {
		MongoDBFactoryBean cf = new MongoDBFactoryBean();

		Assert.assertEquals("mongodb://scott:tiger@host/db",
				cf.injectCredentials("mongodb://host/db", "scott", "tiger"));

	}


	@Test
	public void testConfig() {
		Assert.assertNotNull(applicationContext.getBean("testMongo"));
		
		Assert.assertNotNull(applicationContext.getBean("testMongoDB"));
	}
	
	@Test
	public void testFinder() throws ClassNotFoundException {
		ServiceFactoryClassFinder f = new ServiceFactoryClassFinder();
		Assert.assertEquals(MongoDBFactoryBean.class,f.forServiceType("mongo"));
		Assert.assertEquals(MongoDBFactoryBean.class,f.forServiceType("mongodb"));
	}

}

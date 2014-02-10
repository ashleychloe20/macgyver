package io.macgyver.mongodb;

import io.macgyver.mongodb.MongoClientFactory.ExtendedMongoClient;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoClientFactoryTest extends MacgyverIntegrationTest {



	@Test(expected=IllegalArgumentException.class)
	public void testCredentialInjectorIllegalArgument() {
		MongoClientFactory cf = new MongoClientFactory();
		
		cf.injectCredentials("something", "abc", "def");
	}
	
	@Test()
	public void testCredentialInjectorNoCredentials() {
		MongoClientFactory cf = new MongoClientFactory();
		
		Assert.assertEquals("mongodb://host/db",cf.injectCredentials("mongodb://host/db", null, null));
		Assert.assertEquals("mongodb://host/db",cf.injectCredentials("mongodb://host/db", "", ""));
	}
	
	@Test()
	public void testCredentialInjectorWIthCredentials() {
		MongoClientFactory cf = new MongoClientFactory();
		
		Assert.assertEquals("mongodb://scott:tiger@host/db",cf.injectCredentials("mongodb://host/db", "scott", "tiger"));
		
	}
	
	@Test
	public void testLazyConstruction() {
		MongoClientFactory cf = new MongoClientFactory();
		cf.setUri("mongodb://localhost:12345/somethingnotfound");
		DB db = cf.createDBConnection();
		Assert.assertNotNull(db);
		
	}

}

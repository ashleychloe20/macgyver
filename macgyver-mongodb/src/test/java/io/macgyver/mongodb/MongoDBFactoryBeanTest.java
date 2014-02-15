package io.macgyver.mongodb;

import java.net.UnknownHostException;

import io.macgyver.config.MongoDBFactoryBean;
import io.macgyver.config.MongoDBFactoryBean.ExtendedMongoClient;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBFactoryBeanTest  {

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



}

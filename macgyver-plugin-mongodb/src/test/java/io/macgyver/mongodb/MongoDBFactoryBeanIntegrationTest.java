package io.macgyver.mongodb;

import io.macgyver.test.MacGyverIntegrationTest;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBFactoryBeanIntegrationTest extends MacGyverIntegrationTest {

	
	@Autowired
	ApplicationContext applicationContext;
	
	
	@Test
	@Ignore
	public void testIt() {
		Mongo m =  applicationContext.getBean("mongotest",Mongo.class);
		
		DB db = applicationContext.getBean("mongotestDB",DB.class);
		
		Assert.assertNotNull(m);
		Assert.assertNotNull(db);
		
	}
	
	@Before
	public void checkMongo() throws UnknownHostException {

		try {
			MongoClientURI curi = new MongoClientURI(
					"mongodb://localhost/macgyver_junit");
			MongoClient c = new MongoClient(curi);

			DB db = c.getDB("macgyver_junit");

			db.getCollectionNames();
		} catch (Exception e) {
			Assume.assumeTrue(false);
		}
	}
}

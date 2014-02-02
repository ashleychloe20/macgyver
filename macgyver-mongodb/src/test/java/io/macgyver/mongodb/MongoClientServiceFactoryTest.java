package io.macgyver.mongodb;

import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.json.JsonObject;

import io.macgyver.core.ConfigStore;
import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

public class MongoClientServiceFactoryTest extends MacgyverIntegrationTest {

	@Autowired
	ConfigStore configStore;

	@Autowired
	MongoClientServiceFactory mongoClientServiceFactory;
	
	@Before
	public void checkMongoAvailable() {

		try {
			JsonObject obj = configStore.getRootConfig()
					.getJsonObject("services").getJsonObject("mongodb")
					.getJsonObject("test");

			MongoClientURI mcu = new MongoClientURI(obj.getString("url"));
			MongoClient c = new MongoClient(mcu);
			c.getDB("macgyver_junit").getCollectionNames();
			// OK, we've connected
			return;
			
	
		} catch (Exception e) {
			Assume.assumeTrue("mongo available", false);
		}
		
	}

	@Test
	public void testX() {
		mongoClientServiceFactory.getDB("test").getCollectionNames();
	}
}

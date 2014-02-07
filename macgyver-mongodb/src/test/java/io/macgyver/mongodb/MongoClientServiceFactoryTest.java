package io.macgyver.mongodb;

import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MongoClientServiceFactoryTest extends MacgyverIntegrationTest {


	/*
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
	*/

	@Test
	public void testX() {
		
	}

}

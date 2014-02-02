package io.macgyver.mongodb;

import javax.json.JsonObject;

import io.macgyver.core.ServiceFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;


public class MongoDBServiceFactory extends ServiceFactory<DB> {

	public MongoDBServiceFactory() {
		super("mongodb");
	}

	@Autowired
	MongoClientServiceFactory clientServiceFactory;
	
	@Override
	public DB create(String name, JsonObject cfg) {
		return clientServiceFactory.getDB(name);
	}

}

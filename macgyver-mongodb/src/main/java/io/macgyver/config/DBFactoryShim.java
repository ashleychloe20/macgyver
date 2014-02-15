package io.macgyver.config;

import io.macgyver.config.MongoDBFactoryBean.ExtendedMongoClient;

import org.springframework.beans.factory.FactoryBean;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DBFactoryShim implements FactoryBean<DB>  {

	MongoClient client;
	
	DBFactoryShim(MongoClient client) {
		this.client = client;
	}
	@Override
	public DB getObject() throws Exception {
		
		
		ExtendedMongoClient extendedMongoClient = (ExtendedMongoClient) client;
		return extendedMongoClient.getDB(extendedMongoClient.getDatabaseName());
	}

	@Override
	public Class<?> getObjectType() {
		return DB.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}


}

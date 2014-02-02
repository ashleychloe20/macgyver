package io.macgyver.mongodb;

import io.macgyver.core.ConfigurationException;
import io.macgyver.core.MultiToolException;

import java.net.UnknownHostException;

import javax.json.JsonObject;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoClientServiceFactory extends
		io.macgyver.core.ServiceFactory<MongoClient> {

	public MongoClientServiceFactory() {
		super("mongodb");
		
	}

	public class ExtendedMongoClient extends MongoClient {

		MongoClientURI mongoClientUri;
		
		public ExtendedMongoClient(MongoClientURI uri)
				throws UnknownHostException {
			super(uri);
			this.mongoClientUri = uri;
		}
		
	}
	@Override
	public MongoClient create(String name, JsonObject obj) {
		try {
			
		
			String url = obj.getString("url");
			MongoClientURI mci = new MongoClientURI(url);

			ExtendedMongoClient mc = new ExtendedMongoClient(mci);
			
			return mc;
		} catch (UnknownHostException e) {
			throw new MultiToolException(e);
		}
	}
	
	public DB getDB(String name) {
		ExtendedMongoClient c = (ExtendedMongoClient) get(name);
		String dbName = c.mongoClientUri.getDatabase();
		if (dbName==null) {
			throw new ConfigurationException("db name not specified in URL");
		}
		return c.getDB(dbName);
		
	}

}

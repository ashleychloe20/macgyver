package io.macgyver.mongodb;

import io.macgyver.core.ConfigurationException;
import io.macgyver.core.ServiceFactory;

import java.net.UnknownHostException;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoClientFactory extends ServiceFactory<MongoClient> {

	public MongoClientFactory() {

	}

	String uri = null;
	String username = null;
	String password = null;

	public MongoClient create() {
		try {
			return new ExtendedMongoClient(new MongoClientURI(
					injectCredentials(getUri(), getUsername(), getPassword())));
		} catch (UnknownHostException e) {
			throw new ConfigurationException(e);
		}
	}

	public DB createDBConnection() {
		ExtendedMongoClient c = (ExtendedMongoClient) create();
		return c.getDB(c.getDatabaseName());
		
	}
	public class ExtendedMongoClient extends MongoClient {

		MongoClientURI mongoClientUri;

		public ExtendedMongoClient(MongoClientURI uri)
				throws UnknownHostException {
			super(uri);
			this.mongoClientUri = uri;

		}

		public String getDatabaseName() {
			return mongoClientUri.getDatabase();
		}
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DB getDB() {
		return createDBConnection();
	}
	/*
	 * public DB getDB(String name) { ExtendedMongoClient c =
	 * (ExtendedMongoClient) get(name); String dbName =
	 * c.mongoClientUri.getDatabase(); if (dbName==null) { throw new
	 * ConfigurationException("db name not specified in URL"); } return
	 * c.getDB(dbName);
	 * 
	 * }
	 */

	String injectCredentials(String uri, String username, String password) {
		Preconditions.checkNotNull(uri);
		Preconditions.checkArgument(uri.startsWith("mongodb://"),
				"mongo uri must start with mongodb://");
		if (!Strings.isNullOrEmpty(username)
				&& !Strings.isNullOrEmpty(password)) {
			if (uri.contains("@")) {
				throw new IllegalArgumentException(
						"mongo uri must not contain credentials if you are injecting them");
			}
			uri = uri.replace("mongodb://", "mongodb://" + username + ":"
					+ password + "@");
			return uri;

		} else {
			return uri;
		}
	}
}

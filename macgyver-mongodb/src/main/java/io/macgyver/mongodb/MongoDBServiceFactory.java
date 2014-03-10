package io.macgyver.mongodb;

import io.macgyver.core.CollaboratorRegistrationCallback;
import io.macgyver.core.ConfigurationException;
import io.macgyver.core.factory.ServiceFactory;

import java.net.UnknownHostException;
import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Component
public class MongoDBServiceFactory extends ServiceFactory<MongoClient> {

	@Override
	public void configure(String name, Properties props) {
		super.configure(name, props);
		addCollaboratorRelationship(name+"DB", name);
	}

	@Override
	protected void registerCollaborators(String name, Object primary) {
		super.registerCollaborators(name, primary);
		ExtendedMongoClient client = (ExtendedMongoClient) primary;
		
		registry.registerCollaborator(name+"DB", client.getDB(client.getDatabaseName()));
	}

	public MongoDBServiceFactory() {
		super("mongodb");
	}

	public MongoClient createObject(Properties props) {
		try {
			return new ExtendedMongoClient(new MongoClientURI(
					injectCredentials(props.getProperty("uri"),
							props.getProperty("username"),
							props.getProperty("password"))));
		} catch (UnknownHostException e) {
			throw new ConfigurationException(e);
		}
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

	



	public String injectCredentials(String uri, String username, String password) {
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

	public static class DBFactoryShim implements FactoryBean<DB> {

		MongoClient client;

		DBFactoryShim(MongoClient client) {
			this.client = client;
		}

		@Override
		public DB getObject() throws Exception {

			ExtendedMongoClient extendedMongoClient = (ExtendedMongoClient) client;
			return extendedMongoClient.getDB(extendedMongoClient
					.getDatabaseName());
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
}

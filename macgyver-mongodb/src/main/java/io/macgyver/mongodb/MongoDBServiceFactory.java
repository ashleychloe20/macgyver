package io.macgyver.mongodb;

import io.macgyver.core.ConfigurationException;
import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceInstanceRegistry;

import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Component
public class MongoDBServiceFactory extends ServiceFactory<MongoClient> {

	

	public MongoDBServiceFactory() {
		super("mongodb");
	}

	public MongoClient doCreateInstance(ServiceDefinition def) {
		Properties props = def.getProperties();
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





	@Override
	protected void doCreateCollaboratorInstances(
			ServiceInstanceRegistry registry,
			ServiceDefinition primaryDefinition, MongoClient primaryBean) {
		
		ExtendedMongoClient c = (ExtendedMongoClient) primaryBean;
		DB db = c.getDB(c.getDatabaseName());
		
		registry.registerCollaborator(primaryDefinition.getName()+"DB", db);
		
	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {
		ServiceDefinition dbDef = new ServiceDefinition(def.getName()+"DB", def.getName(), def.getProperties(), this);
		defSet.add(dbDef);
	}
}

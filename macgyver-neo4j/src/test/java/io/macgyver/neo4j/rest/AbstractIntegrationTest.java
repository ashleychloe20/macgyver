package io.macgyver.neo4j.rest;

import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.LoggerFactory;

public class AbstractIntegrationTest {

	static Boolean available=null;
	private Neo4jRestClient client;
	
	
	public String getNeo4jRestUrl() {
		String val = System.getProperty("neo4j.url","http://localhost:7474");
		return val;
	}
	
	public synchronized Neo4jRestClient getClient() {
		if (client==null) {
			client = new Neo4jRestClient(getNeo4jRestUrl()); 
		}
		return client;
	}
	@Before
	public synchronized void checkIfNeo4jIsAvailable() {
		
		if (available==null) {
			available = new Neo4jRestClient().checkOnline();
		}
		if (!available) {
			LoggerFactory.getLogger(AbstractIntegrationTest.class).warn("neo4j is not available -- integration tests will not be run");
		}
		Assume.assumeTrue(available);
	}
}

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.neo4j.rest;

import org.jboss.resteasy.logging.Logger;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.LoggerFactory;

public class AbstractIntegrationTest {

	protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
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
			logger.warn("neo4j is not available -- integration tests will not be run");
		}
		Assume.assumeTrue(available);
	}
}

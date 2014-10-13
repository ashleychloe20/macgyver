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

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Neo4jRestClientIntegrationTest extends AbstractIntegrationTest{

	
	@Test
	public void testX() {
		

		// ObjectNode n = c.execCypher("match v  return ID(v),v.name limit 3",
		// new ObjectMapper().createObjectNode());

		String name = "" + System.currentTimeMillis();
		ObjectNode n = getClient()
				.execCypherWithJsonResponse("CREATE (user:User { Id: 456, name: '"
						+ name + "' })");

		n = getClient().execCypherWithJsonResponse("match v where v.name='" + name
				+ "' return v limit 3");
	

		Result rs = getClient().execCypher("match v where v.name='" + name
				+ "' return v.name, v.age , v limit 3");

		while (rs.next()) {
			logger.debug("v.age: {}" , rs.getString("v.age"));

			logger.debug("vertex props: {}",rs.getObjectNode("v").path("data"));
		}
	}
}

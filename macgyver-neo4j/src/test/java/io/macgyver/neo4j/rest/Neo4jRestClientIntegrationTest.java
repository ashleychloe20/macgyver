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

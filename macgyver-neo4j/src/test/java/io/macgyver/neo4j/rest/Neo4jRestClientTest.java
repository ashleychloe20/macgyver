package io.macgyver.neo4j.rest;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Neo4jRestClientTest {

	@Test
	public void testX() {
		Neo4jRestClient c = new Neo4jRestClient();

		// ObjectNode n = c.execCypher("match v  return ID(v),v.name limit 3",
		// new ObjectMapper().createObjectNode());

		String name = "" + System.currentTimeMillis();
		ObjectNode n = c
				.execCypherWithJsonResponse("CREATE (user:User { Id: 456, name: '"
						+ name + "' })");

		n = c.execCypherWithJsonResponse("match v where v.name='" + name
				+ "' return v limit 3");
		System.out.println(n);

		Result rs = c.execCypher("match v where v.name='" + name
				+ "' return v.name, v.age , v limit 3");

		while (rs.next()) {
			System.out.println("XXX: " + rs.getString("v.age"));

			System.out.println(rs.getVertex("v").getProperties());
		}
	}

	@Test
	public void testIntParam() {
		Neo4jRestClient c = new Neo4jRestClient();

		ObjectNode n = c.createParameters("abc", 1);

	}
	@Test
	public void testLongParam() {
		Neo4jRestClient c = new Neo4jRestClient();

		ObjectNode n = c.createParameters("abc", 1L);

	}
	@Test
	public void testDoubleParam() {
		Neo4jRestClient c = new Neo4jRestClient();

		ObjectNode n = c.createParameters("abc", 1.5d);

	}
	@Test
	public void testBooleanParam() {
		Neo4jRestClient c = new Neo4jRestClient();

		ObjectNode n = c.createParameters("abc", true);

	}
	@Test
	public void testParams() {
		Neo4jRestClient c = new Neo4jRestClient();

		ObjectNode n = c.createParameters("abc", "def", "xxx", "yyy");

		org.junit.Assert.assertEquals(2, n.size());
		Assert.assertEquals(0, c.createParameters().size());

		Assert.assertEquals("def", c.createParameters("abc", "def").path("abc")
				.asText());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateParamsWithOddArgs() {
		Neo4jRestClient c = new Neo4jRestClient();

		ObjectNode n = c.createParameters("abc", "def", "xxx");
		System.out.println(n);
	}
}

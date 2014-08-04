package io.macgyver.neo4j.rest;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Neo4jRestClientTest {

	

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
	
	}
}

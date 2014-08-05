package io.macgyver.neo4j.rest;

import io.macgyver.neo4j.rest.impl.NonStreamingResultSetImpl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResultSetImplTest {

	ObjectMapper m = new ObjectMapper();
	
	@Test(expected=Neo4jException.class)
	public void testInvalidArgument1() {
		ObjectNode n = m.createObjectNode();
		
		NonStreamingResultSetImpl rsi = new NonStreamingResultSetImpl(n);
		
	}
	
	@Test(expected=Neo4jException.class)
	public void testInvalidTypesInConstructor() {
		ObjectNode n = m.createObjectNode();
		
		ObjectNode data = m.createObjectNode();
		ObjectNode columns = m.createObjectNode();
		n.set("data", data);
		n.set("columns",columns);
		NonStreamingResultSetImpl rsi = new NonStreamingResultSetImpl(n);
		
	}
	
	@Test
	public void testEmptyResponse() {
		ObjectNode n = m.createObjectNode();
		
		ArrayNode data = m.createArrayNode();
		ArrayNode columns = m.createArrayNode();
		n.set("data", data);
		n.set("columns",columns);
		NonStreamingResultSetImpl rs = new NonStreamingResultSetImpl(n);
		
		Assert.assertFalse(rs.hasNext());
		Assert.assertFalse(rs.next());
		Assert.assertFalse(rs.next());
		Assert.assertFalse(rs.next());
		Assert.assertFalse(rs.hasNext());
		
		
	}
	
	@Test
	public void testSimpleResponse() {
		ObjectNode n = m.createObjectNode();
		
		ArrayNode data = m.createArrayNode();
		ArrayNode columns = m.createArrayNode();
		columns.add("a");
		columns.add("b");
		columns.add("c");
		
		ArrayNode d0 = m.createArrayNode();
		d0.add("a0");
		d0.add("b0");
		d0.add("c0");
		data.add(d0);
		
		ArrayNode d1 = m.createArrayNode();
		d1.add("a1");
		d1.addNull();
		d1.add("c1");
		n.set("data", data);
		n.set("columns",columns);
		
		data.add(d1);
		NonStreamingResultSetImpl rs = new NonStreamingResultSetImpl(n);
		
		Assert.assertTrue(rs.hasNext());
		Assert.assertTrue(rs.next());
		Assert.assertTrue(rs.hasNext());
	
		Assert.assertEquals("a0",rs.getString("a"));
		Assert.assertEquals("b0",rs.getString("b"));
		Assert.assertEquals("c0",rs.getString("c"));
		
		Assert.assertEquals("a0",rs.getString(0));
		Assert.assertEquals("b0",rs.getString(1));
		Assert.assertEquals("c0",rs.getString(2));
		
		rs.next();
		
		Assert.assertEquals("a1",rs.getString(0));
		Assert.assertNull(rs.getString(1));
		Assert.assertEquals("c1",rs.getString(2));
		
		
	}
}

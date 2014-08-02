package io.macgyver.neo4j.rest;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Neo4jRestClientTest {

	
	@Test
	public void testX() {
		Neo4jRestClient c = new Neo4jRestClient();
		
		//ObjectNode n = c.execCypher("match v  return ID(v),v.name limit 3", new ObjectMapper().createObjectNode());
		
		String name = ""+System.currentTimeMillis();
		ObjectNode n = c.execCypherWithJsonResponse("CREATE (user:User { Id: 456, name: '"+name+"' })");
		
		
		n = c.execCypherWithJsonResponse("match v where v.name='"+name+"' return v limit 3");
		System.out.println(n);
		
		
		Result rs = c.execCypher("match v where v.name='"+name+"' return v.name, v.age , v limit 3");
		
		while (rs.next()) {
			System.out.println("XXX: "+rs.getString("v.age"));
			
			System.out.println(rs.getVertex("v").getProperties());
		}
	}
}

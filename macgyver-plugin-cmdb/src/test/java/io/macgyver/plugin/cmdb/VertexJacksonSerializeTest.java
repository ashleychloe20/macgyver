package io.macgyver.plugin.cmdb;

import static org.junit.Assert.assertEquals;
import io.macgyver.core.graph.SimpleVertexSerializer;
import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public class VertexJacksonSerializeTest extends MacGyverIntegrationTest{

	@Autowired
	TransactionalGraph graph;
	
	ObjectMapper mapper = new ObjectMapper();
	
	
	@Test
	public void testSerialize() throws JsonProcessingException {
		new SimpleVertexSerializer().add(mapper);
		try {
			Vertex v = graph.addVertex(null);
			
			v.setProperty("hello", "world");
			
			ObjectNode n = mapper.valueToTree(v);
			
			assertEquals(v.getId().toString(),n.path("_id").asText());
			assertEquals(v.getProperty("hello"),n.path("props").path("hello").asText());
		}
		finally {
			graph.rollback();
		}
	}
}

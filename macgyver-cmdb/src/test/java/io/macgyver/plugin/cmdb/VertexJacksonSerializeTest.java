package io.macgyver.plugin.cmdb;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

import io.macgyver.core.titan.SimpleVertexSerializer;
import io.macgyver.test.MacGyverIntegrationTest;

public class VertexJacksonSerializeTest extends MacGyverIntegrationTest{

	@Autowired
	TitanGraph graph;
	
	ObjectMapper mapper = new ObjectMapper();
	
	
	@Test
	public void testX() throws JsonProcessingException {
		new SimpleVertexSerializer().add(mapper);
		try {
			Vertex v = graph.addVertex(null);
			v.setProperty("hello", "world");
			System.out.println(mapper.writeValueAsString(v));
		}
		finally {
			graph.rollback();
		}
	}
}

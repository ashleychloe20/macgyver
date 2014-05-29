package io.macgyver.core.titan;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

import io.macgyver.test.MacGyverIntegrationTest;

public class GraphRepositoryTest extends MacGyverIntegrationTest{

	
	@Autowired
	TitanGraph titanGraph;
	
	
	public static class TestBean {
		String testProperty;

		public String getTestProperty() {
			return testProperty;
		}

		public void setTestProperty(String testProperty) {
			this.testProperty = testProperty;
		}
		
	}
	@Test
	public void testFindOneVertex() {
		GraphRepository gr = new GraphRepository(titanGraph);
		
		String val = UUID.randomUUID().toString();
		Vertex v = gr.getGraph().addVertex(null);
		v.setProperty("testProperty", val);
		gr.getGraph().commit();
		
		Optional<Vertex> vx = gr.findOneVertex("testProperty",val);
		Assert.assertTrue(vx.isPresent());
		
		
		Assert.assertEquals(v.getId(), vx.get().getId());
	}
	@Test
	public void testFindOne() {
		GraphRepository gr = new GraphRepository(titanGraph);
		
		String val = UUID.randomUUID().toString();
		Vertex v = gr.getGraph().addVertex(null);
		v.setProperty("testProperty", val);
		gr.getGraph().commit();
		
		Optional<ObjectNode> vx = gr.findOneObjectNode("testProperty",val);
		Assert.assertTrue(vx.isPresent());
		
		
		Assert.assertEquals(v.getId(),vx.get().path("_id").asLong());
		Assert.assertEquals(v.getProperty("testProperty"),vx.get().path("properties").path("testProperty").asText());
		
	}
	
	@Test
	public void testFindOneBean() {
		GraphRepository gr = new GraphRepository(titanGraph);
		
		String val = UUID.randomUUID().toString();
		Vertex v = gr.getGraph().addVertex(null);
		v.setProperty("testProperty", val);
		gr.getGraph().commit();
		
		Optional<TestBean> vx = gr.findOne("testProperty",val, TestBean.class);
		Assert.assertTrue(vx.isPresent());
		
		
		Assert.assertEquals(v.getProperty("testProperty"),vx.get().getTestProperty());
		
		
	}
	
	@Test
	public void testFindObjectNodes() {
		GraphRepository gr = new GraphRepository(titanGraph);
		
		String val = UUID.randomUUID().toString();
		Vertex v = gr.getGraph().addVertex(null);
		v.setProperty("testProperty", val);
		gr.getGraph().commit();
		
		gr.findObjectNodes("testProperty",val);
		
		Iterable<ObjectNode> vx = gr.findObjectNodes("testProperty",val);
		for (ObjectNode n: vx) {
			Assert.assertEquals(v.getId(),n.path("_id").asLong());
			Assert.assertEquals(v.getProperty("testProperty"),n.path("properties").path("testProperty").asText());	
		}
		
	}
}

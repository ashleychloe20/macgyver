package io.macgyver.plugin.cmdb;

import java.util.List;
import java.util.Map;

import io.macgyver.test.MacGyverIntegrationTest;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

public class AppInstanceManagerTest extends MacGyverIntegrationTest {
	@Autowired
	AppInstanceManager manager;

	@Autowired
	TitanGraph graph;
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnique() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "myapp";
		
		manager.getOrCreateAppInstance(host, appId);
		try {
			Vertex v = graph.addVertex(null);
			v.setProperty("host", host);
			v.setProperty("appId",appId);
			v.setProperty("vertexId", AppInstance.calculateVertexId(host, appId,null));
		}
		finally {
			graph.rollback();
		}
	}
	@Test
	public void testIt() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "myapp";
		
		String vid = AppInstance.calculateVertexId(host, appId,null);
		assertFalse(manager.getAppInstance(host, "myapp").isPresent());
		
		Map<String,Object> p = Maps.newHashMap();
		p.put("version", "12345");
		AppInstance ai = manager.getOrCreateAppInstance(host, "myapp",p );
		assertNotNull(ai);
		
		
		ai = manager.getOrCreateAppInstance(host, appId);
		assertEquals("12345",ai.getProperties().get("version"));
		
		
		

	}
	
	@Test
	public void x() throws Exception {
		
		Assert.assertFalse(manager.getAppInstance("localhost", "test").isPresent());
		AppInstance ai = manager.getOrCreateAppInstance("localhost", "test");
		Assert.assertNotNull(manager.getOrCreateAppInstance("localhost", "test"));
		Assert.assertTrue(manager.getAppInstance("localhost", "test").isPresent());
	
	}
	
	@Test
	public void testSort() throws Exception {

		manager.getOrCreateAppInstance("abc.example.com", "xxx");
		manager.getOrCreateAppInstance("abc.example.com", "aaa");
		
		List<AppInstance> list = Lists.newArrayList();
		Iterables.addAll(list,manager.findAll());
		
		Assert.assertTrue(2==list.size());
		
	}
}

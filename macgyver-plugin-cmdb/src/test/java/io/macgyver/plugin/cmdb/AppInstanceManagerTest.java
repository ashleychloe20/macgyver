package io.macgyver.plugin.cmdb;

import java.util.List;
import java.util.Map;

import io.macgyver.test.MacGyverIntegrationTest;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Ignore;
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
	
	
	@Ignore
	@Test(expected=IllegalArgumentException.class)
	public void testUnique() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "myapp";
		String groupId = "mygroup";
		
		manager.getOrCreateAppInstance(host, groupId, appId);
		try {
			Vertex v = graph.addVertex(null);
			v.setProperty("host", host);
			v.setProperty("artifactId",appId);
			v.setProperty("groupId",groupId);
			v.setProperty("vertexId", AppInstance.calculateVertexId(host, groupId, appId,null));
		}
		finally {
			graph.rollback();
		}
	}
	@Test
	public void testIt() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "myapp";
		String groupId = "group";
		
		String vid = AppInstance.calculateVertexId(host, groupId, appId,null);
		assertFalse(manager.getAppInstance(host, groupId, appId).isPresent());
		
		Map<String,Object> p = Maps.newHashMap();
		p.put("version", "12345");
		AppInstance ai = manager.getOrCreateAppInstance(host, groupId,"myapp",p );
		assertNotNull(ai);
		
		
		ai = manager.getOrCreateAppInstance(host, groupId,appId);
		assertEquals("12345",ai.getProperties().get("version"));
		
		
		

	}
	
	@Test
	public void x() throws Exception {
		
		Assert.assertFalse(manager.getAppInstance("localhost", "group","test").isPresent());
		AppInstance ai = manager.getOrCreateAppInstance("localhost", "group","test");
		Assert.assertNotNull(manager.getOrCreateAppInstance("localhost", "group","test"));
		Assert.assertTrue(manager.getAppInstance("localhost", "group","test").isPresent());
	
	}
	@Test
	public void testXX() {
		int nodesToAdd = 50;
		List<Vertex> list = Lists.newArrayList();
		Iterables.addAll(list,graph.query().has("vertexType", "AppInstance").vertices());
		
		int beforeCount = list.size();
	
		
		for (int i=0; i<nodesToAdd; i++) {
			
			AppInstance ai = manager.getOrCreateAppInstance("host_"+i, "group","app_"+i);
		
			manager.save(ai);
		
		}
		list.clear();
		Iterables.addAll(list,graph.query().has("vertexType", "AppInstance").vertices());
		int afterCount = list.size();
		Assert.assertEquals(nodesToAdd,afterCount-beforeCount);
		
	}
	@Test
	public void testSort() throws Exception {

		manager.getOrCreateAppInstance("abc.example.com","group" ,"xxx");
		manager.getOrCreateAppInstance("abc.example.com", "group","aaa");
		
		List<AppInstance> list = Lists.newArrayList();
		Iterables.addAll(list,manager.find());
		
		Assert.assertTrue(2==list.size());
		
	}
}

package io.macgyver.plugin.cmdb;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
		
		manager.getOrCreateAppInstanceVertex(host, groupId, appId);
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
		assertNotNull(manager.getOrCreateAppInstanceVertex(host, groupId, appId));
		
		
		
		
		

	}
	
	@Test
	public void x() throws Exception {
		
		
		Assert.assertNotNull(manager.getOrCreateAppInstanceVertex("localhost", "group","test"));
	
	}
	@Test
	public void testXX() {
		int nodesToAdd = 50;
		List<Vertex> list = Lists.newArrayList();
		Iterables.addAll(list,graph.query().has("vertexType", "AppInstance").vertices());
		
		int beforeCount = list.size();
	
		
		for (int i=0; i<nodesToAdd; i++) {
			
			manager.getOrCreateAppInstanceVertex("host_"+i, "group","app_"+i);
			manager.getOrCreateAppInstanceVertex("host_"+i, "group","app_"+i);
		
		
		}
		manager.getGraph().commit();
		list.clear();
		Iterables.addAll(list,graph.query().has("vertexType", "AppInstance").vertices());
		int afterCount = list.size();
		Assert.assertEquals(nodesToAdd,afterCount-beforeCount);
		
	}

}

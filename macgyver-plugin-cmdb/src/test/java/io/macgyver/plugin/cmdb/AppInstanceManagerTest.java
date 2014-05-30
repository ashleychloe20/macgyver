package io.macgyver.plugin.cmdb;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

	SecureRandom secureRandom;
	
	public AppInstanceManagerTest() throws NoSuchAlgorithmException{
		super();
		
		secureRandom = SecureRandom.getInstance("sha1prng");
	}
	@Test(expected = IllegalArgumentException.class)
	public void testUnique() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "myapp";
		String groupId = "mygroup";

		manager.getOrCreateAppInstanceVertex(host, groupId, appId);
		try {
			Vertex v = graph.addVertex(null);
			v.setProperty("host", host);
			v.setProperty("artifactId", appId);
			v.setProperty("groupId", groupId);
			v.setProperty("vertexId",
					AppInstance.calculateVertexId(host, groupId, appId, null));
		} finally {
			graph.rollback();
		}
	}

	@Test
	public void testIt() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "myapp";
		String groupId = "group";

		String vid = AppInstance.calculateVertexId(host, groupId, appId, null);
		assertNotNull(manager
				.getOrCreateAppInstanceVertex(host, groupId, appId));

	}

	@Test
	public void x() throws Exception {

		Assert.assertNotNull(manager.getOrCreateAppInstanceVertex("localhost",
				"group", "test"));

	}

	@Test
	public void testXX() {
		int nodesToAdd = 50;
		List<Vertex> list = Lists.newArrayList();
		Iterables.addAll(list, graph.query().has("vertexType", "AppInstance")
				.vertices());

		int beforeCount = list.size();

		for (int i = 0; i < nodesToAdd; i++) {

			manager.getOrCreateAppInstanceVertex("host_" + i, "group", "app_"
					+ i);
			manager.getOrCreateAppInstanceVertex("host_" + i, "group", "app_"
					+ i);

		}
		manager.getGraph().commit();
		list.clear();
		Iterables.addAll(list, graph.query().has("vertexType", "AppInstance")
				.vertices());
		int afterCount = list.size();
		Assert.assertEquals(nodesToAdd, afterCount - beforeCount);

	}
	
	protected int randomInt() {
		return secureRandom.nextInt();
	}
	@Test
	public void testMultiThead() {

		long t0 = System.currentTimeMillis();

		BlockingQueue<Runnable> q = new ArrayBlockingQueue<>(10000);
		for (int i = 0; i < 10000; i++) {
			final Runnable r = new Runnable() {

				@Override
				public void run() {
					String x = "id_" + (randomInt()% 200);
					Vertex v = manager.getOrCreateAppInstanceVertex(x, x, x);
					v.setProperty("someproperty_"
							+ (new Random().nextInt() % 5), UUID.randomUUID()
							.toString());

					try {
						manager.getGraph().commit();
					} catch (RuntimeException e) {
						logger.warn("problem committing transaction: {}",
								e.toString());
					}
				}

			};
			q.add(r);
		}

		ThreadPoolExecutor x = new ThreadPoolExecutor(5, 5, 5,
				TimeUnit.SECONDS, q);

		x.prestartAllCoreThreads();

		while (!q.isEmpty()) {
			try {
				Thread.sleep(500L);
			} catch (Exception e) {
			}
		}

		long t1 = System.currentTimeMillis();
		System.out.println(t1 - t0);
	}

}

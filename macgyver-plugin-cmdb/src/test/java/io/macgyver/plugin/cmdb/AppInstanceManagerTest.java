package io.macgyver.plugin.cmdb;

import static org.junit.Assert.assertNotNull;
import io.macgyver.test.MacGyverIntegrationTest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

public class AppInstanceManagerTest extends MacGyverIntegrationTest {
	@Autowired
	AppInstanceManager manager;

	@Autowired
	TransactionalGraph graph;

	SecureRandom secureRandom;
	
	public AppInstanceManagerTest() throws NoSuchAlgorithmException{
		super();
		
		secureRandom = SecureRandom.getInstance("sha1prng");
	}
	@Test
	public void testUnique() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "myapp";
		String groupId = "mygroup";

		Vertex v1 = manager.getOrCreateAppInstanceVertex(host, groupId, appId);

		Vertex v2 = manager.getOrCreateAppInstanceVertex(host, groupId, appId);
		
		Assert.assertEquals(v1.getId(),v2.getId());
		
		graph.commit();
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
	public void testMultiThead() throws IOException {

		int threadCount=5;
		long t0 = System.currentTimeMillis();
		int iterationCount=5000;
		final int keySpace=5;
		BlockingQueue<Runnable> q = new ArrayBlockingQueue<>(iterationCount);
		for (int i = 0; i < iterationCount; i++) {
			final Runnable r = new Runnable() {

				@Override
				public void run() {
					try {
					String x = "id_" + (randomInt()% 20);
					Vertex v = manager.getOrCreateAppInstanceVertex(x, x, x);
					v.setProperty("someproperty_"
							+ (new Random().nextInt() % keySpace), UUID.randomUUID()
							.toString());

					
						manager.getGraph().commit();
					} catch (RuntimeException e) {
						logger.warn("problem committing transaction: {}",
								e.toString());
					}
				}

			};
			q.add(r);
		}

		ThreadPoolExecutor x = new ThreadPoolExecutor(threadCount, threadCount, 5,
				TimeUnit.SECONDS, q);

		x.prestartAllCoreThreads();

		while (!q.isEmpty()) {
			try {
				Thread.sleep(10L);
			} catch (Exception e) {
			}
		}

		long t1 = System.currentTimeMillis();
		long tdur = t1-t0;
		logger.info("processed {} operations in {}ms using {} threads",iterationCount,tdur, threadCount);
	
		
		GraphSONWriter.outputGraph(graph, System.out);
	}

}

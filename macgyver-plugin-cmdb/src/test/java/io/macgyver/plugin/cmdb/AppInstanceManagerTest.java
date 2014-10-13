/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class AppInstanceManagerTest extends MacGyverIntegrationTest {
	@Autowired
	AppInstanceManager manager;

	SecureRandom secureRandom;

	public AppInstanceManagerTest() throws NoSuchAlgorithmException {
		super();

		secureRandom = SecureRandom.getInstance("sha1prng");
	}

	@Test
	public void testUnique() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "myapp";
		String groupId = "mygroup";

		ObjectNode v1 = manager.getOrCreateAppInstance(host, groupId,
				appId);

		ObjectNode v2 = manager.getOrCreateAppInstance(host, groupId,
				appId);

	}

	@Test
	public void testIt() {
		String host = "unknown_" + System.currentTimeMillis();
		String appId = "junit_myapp";
		String groupId = "junit_group";

		assertNotNull(manager
				.getOrCreateAppInstance(host, groupId, appId));

	}

	@Test
	public void x() throws Exception {

		Assert.assertNotNull(manager.getOrCreateAppInstance("localhost",
				"junit_group", "junit_test"));

	}

	protected int randomInt() {
		return secureRandom.nextInt();
	}

	@Test
	public void testMultiThead() throws IOException {
		ThreadPoolExecutor x = null;
		try {
			int threadCount = 5;
			long t0 = System.currentTimeMillis();
			int iterationCount = 1000;
			final int keySpace = 50;

			BlockingQueue<Runnable> q = new ArrayBlockingQueue<>(iterationCount);
			for (int i = 0; i < iterationCount; i++) {
				final Runnable r = new Runnable() {

					@Override
					public void run() {

						String x = "junit_group_"
								+ (Math.abs(randomInt()) % keySpace);
						ObjectNode v = manager.getOrCreateAppInstance(x,
								x, x);
						/*
						 * v.setProperty("someproperty_" + (new
						 * Random().nextInt() % keySpace), UUID.randomUUID()
						 * .toString());
						 */

					}

				};
				q.add(r);
			}

			x = new ThreadPoolExecutor(threadCount, threadCount, 5,
					TimeUnit.SECONDS, q);

			x.prestartAllCoreThreads();

			while (!q.isEmpty()) {
				try {
					Thread.sleep(10L);
				} catch (Exception e) {
				}
			}

			long t1 = System.currentTimeMillis();
			long tdur = t1 - t0;
			logger.info(
					"processed {} getOrCreateAppInstance calls in {}ms ({}/sec) using {} threads",
					iterationCount, tdur,
					((double) iterationCount / tdur) * 1000, threadCount);
		} finally {
			if (x!=null) {
				x.shutdown();
			}
		}
	}

}

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
package io.macgyver.core.cluster;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;

import io.macgyver.test.MacGyverIntegrationTest;

public class ClusterManagerTest extends MacGyverIntegrationTest {

	@Autowired
	ClusterManager clusterManager;
	
	@Test
	public void testCluster() {
		System.out.println(clusterManager);
		
		Config cfg = new Config();
		cfg.getGroupConfig().setName("macgyver");
		HazelcastInstance i = Hazelcast.newHazelcastInstance(cfg);
		
		
		System.out.println(i.getCluster().getMembers());
		
		System.out.println(clusterManager.isMaster());
		
	}
}

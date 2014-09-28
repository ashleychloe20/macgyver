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

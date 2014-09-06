package io.macgyver.core;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.HazelcastInstance;

import io.macgyver.test.MacGyverIntegrationTest;

public class HazelcastTest extends MacGyverIntegrationTest {

	@Autowired
	HazelcastInstance hazelcast;
	
	@Test
	public void testHazelcast() {
		assertNotNull(hazelcast);
		
		Map<Object,Object> m1 = hazelcast.getMap("test");
		Map<Object,Object> m2 = hazelcast.getMap("test");
		
		m1.put("abc", "123");
		Assert.assertSame(m1,m2);
		Assert.assertEquals("123",m2.get("abc"));
	}
}

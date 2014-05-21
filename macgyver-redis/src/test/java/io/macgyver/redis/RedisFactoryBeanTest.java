package io.macgyver.redis;

import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



public class RedisFactoryBeanTest extends MacGyverIntegrationTest {

	@Autowired
	JedisPool pool;
	
	@Ignore
	@Test
	public void testIt() {
	
		Assert.assertNotNull(applicationContext.getBean("testRedisPool",JedisPool.class));
		Assert.assertSame(pool, applicationContext.getBean("testRedisPool"));
		
		Jedis j = pool.getResource();
		
		j.hset("a", "b","2");
		
		pool.returnResource(j);
		
		
	}
}

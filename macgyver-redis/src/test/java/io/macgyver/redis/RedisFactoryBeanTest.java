package io.macgyver.redis;

import io.macgyver.test.MacgyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



public class RedisFactoryBeanTest extends MacgyverIntegrationTest {

	@Autowired
	JedisPool pool;
	
	@Test
	public void testIt() {
	
		Assert.assertNotNull(applicationContext.getBean("testRedisPool",JedisPool.class));
		Assert.assertSame(pool, applicationContext.getBean("testRedisPool"));
		
		Jedis j = pool.getResource();
		
		j.hset("a", "b","2");
		
		pool.returnResource(j);
	}
}

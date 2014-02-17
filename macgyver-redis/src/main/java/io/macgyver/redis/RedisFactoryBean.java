package io.macgyver.redis;

import io.macgyver.core.ServiceFactoryBean;

import java.util.Properties;

import org.springframework.beans.BeanWrapperImpl;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisFactoryBean extends ServiceFactoryBean<JedisPool> {

	public RedisFactoryBean() {
		super(JedisPool.class);
	}

	@Override
	public JedisPool createObject() throws Exception {

		
		
		
		JedisPool pool = createPoolForProperties(getProperties());
		
	
		return pool;
	}
	
	
	protected JedisPool createPoolForProperties(Properties props) {
		JedisPoolConfig cfg = new JedisPoolConfig();
		
		BeanWrapperImpl bw = new BeanWrapperImpl(cfg);
		for (Object x: props.keySet()) {
			try {
				String val = props.getProperty(x.toString());
				bw.setPropertyValue(x.toString(), val);
			}
			catch (RuntimeException ignore) {
			
			}
		}
		
		String host = props.getProperty("host","localhost");
		int port = Integer.parseInt(props.getProperty("port",Integer.toString(Protocol.DEFAULT_PORT)));
		int timeout = Integer.parseInt(props.getProperty("timeout",Integer.toString(Protocol.DEFAULT_TIMEOUT)));
		String password = props.getProperty("password"); 
		int database = Integer.parseInt(props.getProperty("database",Integer.toString(Protocol.DEFAULT_DATABASE)));
		String clientName = props.getProperty("clientName");
		return new JedisPool(cfg,host,port,timeout,password,database,clientName);
	}

}

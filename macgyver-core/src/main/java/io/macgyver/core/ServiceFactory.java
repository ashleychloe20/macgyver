package io.macgyver.core;

import groovy.util.ConfigObject;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.json.JsonObject;
import javax.json.JsonString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public abstract class ServiceFactory<T> implements DisposableBean {

	// "default" is a Java/Groovy keyword so it causes issues
	public static final String DEFAULT_SERVICE_NAME="defaultService";
	
	public static final String SERVICES_NODE_NAME="services";
	
	Logger logger = LoggerFactory.getLogger(ServiceFactory.class);

	String serviceTypeName;

	@Autowired
	protected ConfigStore configStore;

	protected Map<String, T> serviceMap = Maps.newConcurrentMap();

	public ServiceFactory(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public Map<String, T> getServiceMap() {
		return Collections.unmodifiableMap(serviceMap);
	}

	public Properties toProperties(JsonObject obj) {
		Properties p = new Properties();
		for (String key : obj.keySet()) {
			Object val = obj.get(key);
			if (val == null) {
				// do nothing
			} else if (val instanceof JsonString) {
				p.put(key, ((JsonString) val).getChars());
			} else {
				logger.error("unhandled type: " + val);
			}
		}
		return p;

	}

	public synchronized T get(String name) {

		T t = serviceMap.get(name);
		if (t == null) {
			JsonObject obj = null;
			try {
				obj = configStore.getRootConfig()
						.getJsonObject(SERVICES_NODE_NAME)
						.getJsonObject(getServiceTypeName())
						.getJsonObject(name);
				
			} catch (NullPointerException e) {
				throw new ServiceNotFoundException(name);
			}
			if (obj == null) {
				throw new ServiceNotFoundException(name);
			}
			t = create(name, obj);
			serviceMap.put(name, t);
		}
		return t;
	}


	public T getDefault() {
		return getDefaultService();
	}

	public T getDefaultService() {
		return get(DEFAULT_SERVICE_NAME);
	}

	public abstract T create(String name, JsonObject cfg);

	protected ConfigStore getConfigStore() {
		return configStore;
	}

	public void destroy() {
		for (T t : serviceMap.values()) {
			if (t instanceof DisposableBean) {
				try {
					DisposableBean db = (DisposableBean) t;
					db.destroy();
				} catch (Exception e) {
					logger.warn("", e);
				}
			}
		}

	}
}

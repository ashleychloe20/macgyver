package io.macgyver.core.factory;

import io.macgyver.core.MacGyverPropertySourcesPlaceholderConfigurer;
import io.macgyver.core.ServiceNotFoundException;

import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class ServiceInstanceRegistry {

	Logger logger = LoggerFactory.getLogger(ServiceInstanceRegistry.class);
	
	protected Map<String, ServiceFactory> serviceFactoryMap = Maps.newConcurrentMap();

	Map<String,Object> instances = Maps.newConcurrentMap();
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	MacGyverPropertySourcesPlaceholderConfigurer cfg;

	
	public Object get(String name) {
		
		Object instance = instances.get(name);
		
		// check for an already-constructed service
		if (instance!=null) {
			return instance;
		}
		
		// since we don't have a service ready, we need to delegate to the appropriate factory
		String serviceType = cfg.getEffectiveProperties().getProperty(name+".serviceType");
		if (serviceType==null) {
			throw new ServiceNotFoundException(name);
		}
		ServiceFactory sf = serviceFactoryMap.get(serviceType);
		
		if (sf == null) {
			throw new ServiceNotFoundException("unknown service type '"+serviceType+"' specified for service '"+name+"'");
		} 
		
		Optional<Object> x = (Optional<Object>) sf.get(name);
		
		if (x.isPresent()) {
			return x.get();
		}
		throw new ServiceNotFoundException("unknwon service '"+name+"'");
	
		
	}



	@PostConstruct
	public void scanConfig() {
	
		collectServiceFactories();
		
		Properties properties = cfg.getEffectiveProperties();
		for (Object keyObj : properties.keySet()) {
			String key = keyObj.toString();
			if (isServiceTypeKey(key)) {
				String serviceType = properties.getProperty(key);
				
				ServiceFactory factory = serviceFactoryMap.get(serviceType);
				
				
				String serviceName = key.substring(0, key.length()
						- ".serviceType".length());
				
				Properties scopedProperties = extractScopedPropertiesForService(
						properties, serviceName);
				
				factory.configure(serviceName,scopedProperties);
				
			
			}
		}
	}

	protected boolean isServiceTypeKey(String key) {
		return key != null && key.endsWith(".serviceType");
	}

	Properties extractScopedPropertiesForService(Properties p,
			String serviceName) {
		Properties scoped = new Properties();
		for (Object keyObj : p.keySet()) {
			String key = keyObj.toString();

			if (key.startsWith(serviceName + ".")) {
				String val = p.getProperty(key);
				String scopedKey = key.substring(serviceName.length()+1);
				scoped.put(scopedKey, val);
			}
		}
		logger.debug("properties for service '"+serviceName+"': {}",scoped.keySet());
		return scoped;
	}
	
	void collectServiceFactories() {
		
		for (ServiceFactory sf: applicationContext.getBeansOfType(ServiceFactory.class).values()) {
			logger.info("registering service factory: {}",sf);
			serviceFactoryMap.put(sf.getServiceType(), sf);
		}
		
	
		
	}
}

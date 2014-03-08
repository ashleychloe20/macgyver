package io.macgyver.core.factory;

import io.macgyver.core.MacGyverPropertySourcesPlaceholderConfigurer;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public abstract class ServiceFactory<T> implements ApplicationContextAware {

	String serviceType;

	Map<String, Properties> definitions = Maps.newConcurrentMap();
	
	@Autowired
	protected ServiceInstanceRegistry registry;

	protected ApplicationContext applicationContext;

	@Autowired
	MacGyverPropertySourcesPlaceholderConfigurer cfg;

	protected ServiceFactory(String type) {
		this.serviceType = type;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

	protected abstract T createObjecct(Properties props);

	public synchronized Optional<T> get(String name) {
		
		T instance = (T) registry.instances.get(name);
		if (instance!=null) {
			return Optional.of(instance);
		}
		
		Properties props = definitions.get(name);
		if (props==null) {
			return Optional.absent();
		}
		
		
			instance = createObjecct(props);
			registry.instances.put(name, instance);
	
			registerCollaborators(name, instance);
			return Optional.of(instance);
	
	}

	public void configure(String name, Properties props) {
		definitions.put(name, props);
	}

	public String getServiceType() {
		return serviceType;
	}

	protected void registerCollaborators(String name, T primary) {
	}
}

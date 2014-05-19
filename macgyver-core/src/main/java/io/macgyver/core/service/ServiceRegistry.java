package io.macgyver.core.service;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverException;
import io.macgyver.core.ServiceNotFoundException;
import io.macgyver.core.VfsManager;
import io.macgyver.core.crypto.Crypto;
import io.macgyver.core.eventbus.MacGyverEventBus;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;
import org.mapdb.DB;
import org.mapdb.TxBlock;
import org.mapdb.TxMaker;
import org.mapdb.TxRollbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;

public class ServiceRegistry {

	Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

	@SuppressWarnings("rawtypes")
	protected Map<String, ServiceFactory> serviceFactoryMap = Maps
			.newConcurrentMap();

	Map<String, ServiceDefinition> definitions = Maps.newConcurrentMap();
	Map<String, Object> instances = Maps.newConcurrentMap();

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	MacGyverEventBus syncBus;

	@Autowired
	Crypto crypto;

	@Autowired
	TxMaker txMaker;

	@SuppressWarnings("unchecked")
	public <T> T get(String name, Class<T> t) {
		return (T) get(name);
	}

	public Object get(String name) {

		Preconditions.checkNotNull(name);
		Object instance = instances.get(name);

		// check for an already-constructed service
		if (instance != null) {
			return instance;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("defs: {}", definitions);
		}
		ServiceDefinition def = definitions.get(name);

		if (def == null) {
			throw new ServiceNotFoundException(name);
		}

		if (def.isCollaborator()) {
			String primaryName = def.getPrimaryName();

			get(primaryName);
			instance = instances.get(name);
		} else {
			instance = def.getServiceFactory().get(name);

		}

		return instance;

	}

	private void registerServiceDefintion(ServiceDefinition def) {
		def.getServiceFactory().doConfigureDefinition(def);
		logger.info("registering service definition: {}", def);
		definitions.put(def.getName(), def);

	}

	@SuppressWarnings("unchecked")
	@Subscribe
	public void startAfterSpringContextInitialized(
			Kernel.KernelStartedEvent event) throws Exception {

		collectServiceFactories();

		Properties properties = reloadProperties();

		for (Object keyObj : properties.keySet()) {
			String key = keyObj.toString();
			if (isServiceTypeKey(key)) {
				String serviceType = properties.getProperty(key);

				@SuppressWarnings("rawtypes")
				ServiceFactory factory = serviceFactoryMap.get(serviceType
						.toLowerCase());

				if (factory == null) {
					logger.warn("No ServiceFactory registered for service type: "
							+ serviceType.toLowerCase());
				} else {

					String serviceName = key.substring(0, key.length()
							- ".serviceType".length());

					Properties scopedProperties = extractScopedPropertiesForService(
							properties, serviceName);

					Set<ServiceDefinition> set = Sets.newHashSet();
					factory.createServiceDefintions(set, serviceName,
							scopedProperties);

					for (ServiceDefinition def : set) {
						registerServiceDefintion(def);
					}
				}
			}
		}
		autoInit();
	}

	void autoInit() {
		List<ServiceDefinition> defList = Lists.newArrayList();

		defList.addAll(definitions.values());
		for (ServiceDefinition def : defList) {
			try {
				if (!def.isLazyInit()) {
					logger.info("starting service: {}", def);
					get(def.getName());
				}
			} catch (Exception e) {
				logger.warn("problem starting service: " + def, e);

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
				String scopedKey = key.substring(serviceName.length() + 1);
				scoped.put(scopedKey, val);
			}
		}

		scoped.remove("serviceType");
		return scoped;
	}

	@SuppressWarnings("rawtypes")
	void collectServiceFactories() {

		for (ServiceFactory sf : applicationContext.getBeansOfType(
				ServiceFactory.class).values()) {
			logger.info("registering service factory: {}", sf);
			serviceFactoryMap.put(sf.getServiceType().toLowerCase(), sf);
		}
	}

	public void registerCollaborator(String name, Object collaborator) {
		instances.put(name, collaborator);
	}

	@SuppressWarnings("rawtypes")
	public ServiceFactory getServiceFactory(String name) {
		ServiceFactory sf = serviceFactoryMap.get(name.toLowerCase());
		if (sf == null) {
			throw new MacGyverException("no ServiceFactory defined for type: "
					+ name.toLowerCase());
		}
		return sf;
	}

	public ServiceMapAdapter mapAdapter() {
		return new ServiceMapAdapter(this);
	}

	public void publish(ServiceCreatedEvent event) {
		syncBus.post(event);
	}

	protected Properties reloadProperties() throws MalformedURLException, IOException {
		Properties p = new Properties();

		
		FileObject configGroovy = applicationContext.getBean(VfsManager.class).getConfigLocation().resolveFile("services.groovy");
		
		

		ConfigSlurper slurper = new ConfigSlurper();
		if (Kernel.getExecutionProfile().isPresent()) {
			slurper.setEnvironment(Kernel.getExecutionProfile().get());
		}
		
		ConfigObject obj = slurper.parse(configGroovy.getURL());

		p = obj.toProperties();

		p = crypto.decryptProperties(p);

		TxBlock b = new TxBlock() {

			@Override
			public void tx(DB db) throws TxRollbackException {
		

			}
		};
		txMaker.execute(b);
		return p;
		
	}
}

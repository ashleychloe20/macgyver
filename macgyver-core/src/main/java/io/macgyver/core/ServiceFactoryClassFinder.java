package io.macgyver.core;

import io.macgyver.config.CoreConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.io.Closer;


/**
 * ServiceFactoryClassFinder is responsible for locating a ServiceFactoryBean
 * class for a given serviceType name.
 * 
 * @author rschoening
 *
 */
public class ServiceFactoryClassFinder {

	Logger logger = LoggerFactory.getLogger(ServiceFactoryClassFinder.class);

	Map<String, Class> typeMap = null;

	public ServiceFactoryClassFinder() {
		// TODO Auto-generated constructor stub
	}

	public void loadTypeMap(URL url) {
		InputStream is = null;
		Closer c = Closer.create();
		try {
			is = url.openStream();
			c.register(is);
			Properties props = new Properties();
			props.load(is);
			String serviceType = null;
			String factoryBeanClassName = null;
			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				serviceType = null;
				factoryBeanClassName = null;
				try {
					serviceType = entry.getKey().toString();
					factoryBeanClassName = entry.getValue().toString();
					Class clazz = Thread.currentThread()
							.getContextClassLoader()
							.loadClass(factoryBeanClassName);
					typeMap.put(serviceType.toLowerCase(), clazz);
				} catch (Exception e) {
					logger.warn("could not load ServiceFactoryBean "+serviceType+" ==> class "+factoryBeanClassName+" from "+url, e);
				}
			}
		} catch (Exception e) {
			logger.warn("problem loading resource", e);
		} finally {

			try {
				c.close();
			} catch (Exception ignore) {
			}
		}

	}

	public synchronized void loadTypesIfNecessary() {
		if (typeMap == null) {
			typeMap = Maps.newConcurrentMap();
			try {
				
				Enumeration<URL> t = getClass().getClassLoader().getResources(
						"io/macgyver/serviceTypes.properties");
				while (t.hasMoreElements()) {
					URL x = t.nextElement();
					loadTypeMap(x);
				}
			} catch (IOException e) {
				logger.warn("", e);
			}
			
		}

	}

	public Class forServiceType(String serviceType)
			throws ClassNotFoundException {
		loadTypesIfNecessary();
		
		Class clazz = typeMap.get(serviceType.toLowerCase());

		if (clazz==null) {
			logger.warn("serviceType not found: {} valid types={}",serviceType,typeMap.keySet());
		}
		return clazz;
	}
}

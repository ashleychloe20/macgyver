package io.macgyver.core;

import io.macgyver.core.crypto.Crypto;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import com.google.common.collect.Lists;

public class SpringContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	Logger logger = LoggerFactory.getLogger(SpringContextInitializer.class);
	
	Crypto crypto = new Crypto();
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		
		
		Properties overrides = new Properties();
		
		MutablePropertySources sources = applicationContext.getEnvironment().getPropertySources();
		
		// convert the iterator to a list so that we don't encounter ConcurrentModificationException
		List<PropertySource<?>> propertySourceList = Lists.newArrayList(sources.iterator());
		
		for (PropertySource<?> ps: propertySourceList) {
			
			logger.info("ps: "+ps);
			if (ps instanceof EnumerablePropertySource<?>) {
				logger.info("inspecting PropertySource: {}",ps);
				EnumerablePropertySource<?> eps = (EnumerablePropertySource<?>) ps;
				
				String propertyNames[] = eps.getPropertyNames();
				if (propertyNames!=null) {
					for (String name: propertyNames) {
						Object val = eps.getProperty(name);
						
						processProperty(name,val, overrides);
					}
				}
				
			}
		}

		
		sources.addFirst(new PropertiesPropertySource("macgyver-override", overrides));
		
		
	}

	protected void processProperty(String name, Object val, Properties overrides) {
		logger.info("inspecting property: {}",name);
		if (val !=null && val instanceof String) {
			String decryptedValue = crypto.decryptStringWithPassThrough(val.toString());
			if (!val.equals(decryptedValue)) {
				overrides.put(name, val);
			}
		}
	}

}

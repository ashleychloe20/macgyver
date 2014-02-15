package io.macgyver.core;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

@Component
public abstract class ServiceFactoryBean<T> implements FactoryBean<T>,
		ApplicationContextAware {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	ApplicationContext applicationContext;

	Class<? extends T> classType;

	Properties properties = new Properties();

	public ServiceFactoryBean(Class<? extends T> clazz) {
		this.classType = clazz;
	}

	@Override
	public final T getObject() throws Exception {
		T t = createObject();
		autoWire(t);
		return t;
	}

	public abstract T createObject() throws Exception;

	@Override
	public Class<?> getObjectType() {
		return classType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties props) {
		this.properties = props;
	}

	public void setApplicationContext(ApplicationContext ctx) {
		this.applicationContext = ctx;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void autoWire(Object obj) {
		getApplicationContext().getAutowireCapableBeanFactory().autowireBean(
				obj);
	}

	public CollaboratorRegistrationCallback getCollaboratorRegistrationCallback() {
		return null;
	}
	


	protected void assignProperties(Object target, boolean fail) {
		assignProperties(target, getProperties(), fail);
	}

	protected void assignProperties(Object target, Properties props,
			boolean failOnError) {

		BeanWrapperImpl bw = new BeanWrapperImpl(target);
		String key = null;
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			try {
				key = entry.getKey().toString();
				logger.debug("setting {}.{}", target, key);
				Object val = entry.getValue();
				bw.setPropertyValue(key, val);
			} catch (RuntimeException e) {
				if (failOnError) {
					throw e;
				} else {
					logger.warn("error setting {}.{}", target, key);
				}
			}
		}
	}
}

package io.macgyver.core;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import sun.font.CreatedFontTracker;

@Component
public abstract class ServiceFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

	ApplicationContext applicationContext;
	
	Class<? extends T> classType;
	
	Properties properties = new Properties();
	
	public ServiceFactoryBean(Class<? extends T> clazz) {
		this.classType = clazz;
	}
	@Override
	public  final T getObject() throws Exception {
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
		getApplicationContext().getAutowireCapableBeanFactory().autowireBean(obj);
	}
	public void registerCollaborators(BeanDefinitionRegistry x, Properties props) {
		// TODO Auto-generated method stub
		
	}
}

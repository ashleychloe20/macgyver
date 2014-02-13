package io.macgyver.core;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

public abstract class ServiceFactoryBean<T> implements FactoryBean<T> {

	Class<? extends T> classType;
	
	Properties properties = new Properties();
	
	protected ServiceFactoryBean(Class<? extends T> clazz) {
		this.classType = clazz;
	}
	@Override
	public  abstract T getObject() throws Exception;

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

}

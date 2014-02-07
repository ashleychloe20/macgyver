package io.macgyver.core;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

public  abstract class ServiceFactoryBean<T> implements FactoryBean<T> {

	Properties cfg = new Properties();
	
	Class<T> clazz;
	public ServiceFactoryBean(Class<T> clazz) {
		this.clazz = clazz;
	}
	

	@Override
	public Class<T> getObjectType() {
		return clazz;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Properties getCfg() {
		return cfg;
	}


	public void setCfg(Properties cfg) {
		this.cfg = cfg;
	}

}

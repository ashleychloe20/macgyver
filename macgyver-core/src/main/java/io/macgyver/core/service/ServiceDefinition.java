package io.macgyver.core.service;

import java.util.Properties;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class ServiceDefinition {
	String name;
	String primaryName;

	Properties properties = new Properties();

	boolean lazyInit = true;

	@SuppressWarnings("rawtypes")
	ServiceFactory serviceFactory;

	@SuppressWarnings("rawtypes")
	public ServiceDefinition(String name, String primaryName, Properties props,
			ServiceFactory sf) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(primaryName);
		Preconditions.checkNotNull(props);
		Preconditions.checkNotNull(sf);

		this.name = name;
		this.serviceFactory = sf;
		this.properties.putAll(props);
		this.primaryName = primaryName;
	}

	public boolean isCollaborator() {
		return !name.equals(primaryName);
	}

	public String getName() {
		return name;
	}

	public Properties getProperties() {
		return properties;
	}

	@SuppressWarnings("rawtypes")
	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public String getPrimaryName() {
		return primaryName;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("name", name)
				.add("primaryName", primaryName).toString();
	}

	public void setLazyInit(boolean b) {
		this.lazyInit = b;
	}

	public boolean isLazyInit() {
		return lazyInit;
	}
	
	public ServiceDefinition createCollaboratorDefintiion(String suffix) {
		ServiceDefinition def = new ServiceDefinition(getName()+suffix, getName(), getProperties(), getServiceFactory());
		def.setLazyInit(isLazyInit());
		return def;
	}
}

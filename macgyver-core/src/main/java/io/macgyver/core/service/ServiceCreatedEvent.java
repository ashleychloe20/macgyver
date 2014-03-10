package io.macgyver.core.service;

import com.google.common.base.Preconditions;

public class ServiceCreatedEvent {

	ServiceDefinition definition;
	Object instance;

	public ServiceCreatedEvent(ServiceDefinition def, Object service) {
		Preconditions.checkNotNull(service);
		Preconditions.checkNotNull(def);
		this.instance = service;
	}

	public ServiceDefinition getServiceDefinition() {
		return definition;
	}
	
	public Object getServiceInstance() {
		return instance;
	}
}

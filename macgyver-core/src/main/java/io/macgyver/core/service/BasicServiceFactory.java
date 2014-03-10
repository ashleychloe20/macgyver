package io.macgyver.core.service;

import java.util.Set;

public abstract class BasicServiceFactory<T> extends ServiceFactory<T> {

	public BasicServiceFactory(String type) {
		super(type);

	}


	@Override
	protected void doCreateCollaboratorInstances(
			ServiceRegistry registry,
			ServiceDefinition primaryDefinition, T primaryBean) {
		// NO-OP

	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {
		// NO-OP

	}

}

package io.macgyver.plugin.elb.a10;

import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;

import java.util.Set;

public class A10ClientServiceFactory extends ServiceFactory<A10Client> {

	public A10ClientServiceFactory() {
		super("a10");

	}

	@Override
	protected A10Client doCreateInstance(ServiceDefinition def) {

		A10Client c = new A10Client(def.getProperties().getProperty("url"), def
				.getProperties().getProperty("username"), def.getProperties()
				.getProperty("password"));

		try {
			if (!Boolean.parseBoolean(def.getProperties().getProperty(
					"certificateVerificationEnabled", "true"))) {
				c.setCertificateVerificationEnabled(false);
			}
		} catch (Exception e) {
			logger.warn("",e);
		}

		return c;
	}

	@Override
	protected void doCreateCollaboratorInstances(ServiceRegistry registry,
			ServiceDefinition primaryDefinition, A10Client primaryBean) {

	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {

	}

}

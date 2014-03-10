package io.macgyver.core.factory;

import java.util.Set;

import io.macgyver.core.TestBean;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;

public class TestBeanServiceFactory extends ServiceFactory<TestBean> {

	public TestBeanServiceFactory() {
		super("testService");
	}
	@Override
	protected TestBean doCreateInstance(ServiceDefinition def) {
		logger.info("creating testbean with props: "+def.getProperties().keySet());
		return new TestBean();
	}
	@Override
	protected void doCreateCollaboratorInstances(ServiceRegistry registry,
			ServiceDefinition primaryDefinition, TestBean primaryBean) {

	}
	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {

	}





}

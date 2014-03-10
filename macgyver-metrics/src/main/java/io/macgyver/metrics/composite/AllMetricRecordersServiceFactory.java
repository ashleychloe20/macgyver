package io.macgyver.metrics.composite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

@Component
public class AllMetricRecordersServiceFactory extends BasicServiceFactory<AllMetricRecorders>{

	@Autowired
	AllMetricRecorders allRecorders;
	
	public AllMetricRecordersServiceFactory() {
		super("allMetricRecorders");
		
	}

	@Override
	protected AllMetricRecorders doCreateInstance(ServiceDefinition def) {
		return allRecorders;
	}

}

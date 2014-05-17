package io.macgyver.metrics.composite;

import io.macgyver.core.service.ServiceCreatedEvent;
import io.macgyver.metrics.MetricRecorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.eventbus.Subscribe;

public class AllMetricRecorders extends CompositeRecorder {

	@Autowired
	ApplicationContext ctx;

	@Subscribe
	public void receiveStartup(ServiceCreatedEvent event) {
		Object instance = event.getServiceInstance();
		if (instance != null && instance instanceof MetricRecorder) {
			MetricRecorder r = (MetricRecorder) instance;
			if (r != this) {
				addRecorder(r);
			}
		}

	}
}

package io.macgyver.metrics.composite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import io.macgyver.core.Kernel.KernelStartedEvent;
import io.macgyver.metrics.MetricRecorder;

import com.google.common.eventbus.Subscribe;

public class AllMetricRecorders extends CompositeRecorder {

	@Autowired
	ApplicationContext ctx;

	@Subscribe
	public void receiveStartup(KernelStartedEvent event) {
		for (MetricRecorder recorder : ctx.getBeansOfType(MetricRecorder.class).values()) {
			if (recorder != this) {
				addRecorder(recorder);
			}
		}
	}
}

package io.macgyver.config;

import io.macgyver.metrics.composite.AllMetricRecorders;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.AllowConcurrentEvents;

@Configuration
public class MetricsConfig {

	
	@Bean(name="allMetricRecorders")
	public AllMetricRecorders allMetricRecorders() {
		return new AllMetricRecorders();
	}
	
}

package io.macgyver.config;

import io.macgyver.metrics.composite.AllMetricRecorders;
import io.macgyver.metrics.graphite.HostedGraphiteServiceFactory;
import io.macgyver.metrics.leftronic.LeftronicServiceFactory;
import io.macgyver.metrics.librato.LibratoServiceFactory;
import io.macgyver.metrics.statsd.StatsDServiceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.AllowConcurrentEvents;

@Configuration
@ComponentScan(basePackageClasses = { LeftronicServiceFactory.class,
		HostedGraphiteServiceFactory.class, LibratoServiceFactory.class,
		StatsDServiceFactory.class })
public class MetricsConfig {

	@Bean(name = "allMetricRecorders")
	public AllMetricRecorders allMetricRecorders() {
		return new AllMetricRecorders();
	}

	

}

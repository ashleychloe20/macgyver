package io.macgyver.plugin.config;

import io.macgyver.plugin.metrics.graphite.GraphiteReporterServiceFactory;
import io.macgyver.plugin.metrics.leftronic.LeftronicServiceFactory;

import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Slf4jReporter;

@Configuration
@ComponentScan(basePackageClasses = {GraphiteReporterServiceFactory.class,LeftronicServiceFactory.class})
public class MetricsConfig {



	@Bean(name = "macMetricRegistry")
	public MetricRegistry macMetricRegistry() {
		MetricRegistry registry = SharedMetricRegistries
				.getOrCreate("macMetricRegistry");

		
		Slf4jReporter r = Slf4jReporter.forRegistry(registry)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.convertRatesTo(TimeUnit.SECONDS).outputTo(LoggerFactory.getLogger("io.macgyver.metrics")).build();

		r.start(60, TimeUnit.SECONDS);
		
		return registry;
	}

}

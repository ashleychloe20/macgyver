package io.macgyver.metrics.statsd;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class StatsDServiceFactory extends BasicServiceFactory<StatsD> {

	public StatsDServiceFactory() {
		super("statsd");
	}

	@Override
	public StatsD doCreateInstance(ServiceDefinition def) {
		Properties props = def.getProperties();
		StatsD s = new StatsD(props.getProperty("host"), Integer.parseInt(props
				.getProperty("port", "8125")), props.getProperty("prefix"));
		return s;

	}

	@Override
	public void doConfigureDefinition(ServiceDefinition def) {
		def.setLazyInit(false);
	}
	
	
	
}

package io.macgyver.metrics.statsd;

import io.macgyver.core.factory.ServiceFactory;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class StatsDServiceFactory extends ServiceFactory<StatsD> {

	public StatsDServiceFactory() {
		super("statsd");
	}

	@Override
	public StatsD createObject(Properties props)  {
		StatsD s = new StatsD(props.getProperty("host"),
				Integer.parseInt(props.getProperty("port", "8125")), props.getProperty("prefix"));
		return s;

	}
}

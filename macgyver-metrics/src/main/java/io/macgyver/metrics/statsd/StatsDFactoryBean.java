package io.macgyver.metrics.statsd;

import io.macgyver.core.ServiceFactoryBean;

public class StatsDFactoryBean extends ServiceFactoryBean<StatsD> {

	public StatsDFactoryBean() {
		super(StatsD.class);
	}

	@Override
	public StatsD createObject() throws Exception {
		StatsD s = new StatsD(getProperties().getProperty("host"),
				Integer.parseInt(getProperties().getProperty("port", "8125")));
		return s;

	}
}

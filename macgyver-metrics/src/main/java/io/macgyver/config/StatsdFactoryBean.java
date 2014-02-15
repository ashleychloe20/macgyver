package io.macgyver.config;

import io.macgyver.core.ServiceFactoryBean;
import io.macgyver.metrics.statsd.StatsD;

public class StatsdFactoryBean extends ServiceFactoryBean<StatsD> {

	public StatsdFactoryBean() {
		super(StatsD.class);
	}

	@Override
	public StatsD createObject() throws Exception {
		StatsD s = new StatsD(getProperties().getProperty("host"),
				Integer.parseInt(getProperties().getProperty("port", "8125")));
		return s;

	}
}

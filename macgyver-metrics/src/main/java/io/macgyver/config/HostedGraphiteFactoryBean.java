package io.macgyver.config;

import io.macgyver.core.ServiceFactoryBean;
import io.macgyver.metrics.graphite.HostedGraphite;

public class HostedGraphiteFactoryBean extends ServiceFactoryBean<HostedGraphite> {

	public HostedGraphiteFactoryBean() {
		super(HostedGraphite.class);
	}

	@Override
	public HostedGraphite createObject() throws Exception {
		

		HostedGraphite hg = new HostedGraphite();
		hg.setPrefix(getProperties().getProperty("prefix"));
		hg.setApiKey(getProperties().getProperty("apiKey"));
		return hg;
	}
}

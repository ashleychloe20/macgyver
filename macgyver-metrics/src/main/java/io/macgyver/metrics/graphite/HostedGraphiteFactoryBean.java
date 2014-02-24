package io.macgyver.metrics.graphite;

import io.macgyver.core.ServiceFactoryBean;

public class HostedGraphiteFactoryBean extends ServiceFactoryBean<HostedGraphite> {

	public HostedGraphiteFactoryBean() {
		super(HostedGraphite.class);
	}

	@Override
	public HostedGraphite createObject() throws Exception {
		

		HostedGraphite hg = new HostedGraphite();
		hg.setPrefix(getProperties().getProperty("prefix"));
		hg.setApiKey(getProperties().getProperty("apiKey"));
		hg.setQueryBaseUrl(getProperties().getProperty("queryBaseUrl"));
		return hg;
	}
}

package io.macgyver.metrics.graphite;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.ning.http.client.AsyncHttpClient;

import io.macgyver.core.factory.ServiceFactory;

@Component
public class HostedGraphiteServiceFactory extends ServiceFactory<HostedGraphite> {

	public HostedGraphiteServiceFactory() {
		super("hostedgraphite");
	}

	@Override
	public HostedGraphite createObject(Properties props)  {
		
		HostedGraphite hg = new HostedGraphite(new AsyncHttpClient());
		hg.setPrefix(props.getProperty("prefix"));
		hg.setApiKey(props.getProperty("apiKey"));
		hg.setQueryBaseUrl(props.getProperty("queryBaseUrl"));
		return hg;
	}
}

package io.macgyver.metrics.graphite;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.ning.http.client.AsyncHttpClient;

@Component
public class HostedGraphiteServiceFactory extends BasicServiceFactory<HostedGraphite> {

	public HostedGraphiteServiceFactory() {
		super("hostedgraphite");
	}

	@Override
	public HostedGraphite doCreateInstance(ServiceDefinition def)  {
		
		Properties props = def.getProperties();
		
		HostedGraphite hg = new HostedGraphite(new AsyncHttpClient());
		hg.setPrefix(props.getProperty("prefix"));
		hg.setApiKey(props.getProperty("apiKey"));
		hg.setQueryBaseUrl(props.getProperty("queryBaseUrl"));
		return hg;
	}

	@Override
	public void doConfigureDefinition(ServiceDefinition def) {
		def.setLazyInit(false);
	}
	
	
}

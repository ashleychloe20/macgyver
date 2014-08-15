package io.macgyver.plugin.newrelic;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

public class NewRelicServiceFactory extends BasicServiceFactory<NewRelicClient>{

	public NewRelicServiceFactory() {
		super("NewRelic");
		
	}

	@Override
	protected NewRelicClient doCreateInstance(ServiceDefinition def) {
		
		NewRelicClient c = new NewRelicClient();
		c.setApiKey(def.getProperties().getProperty("apiKey"));
		c.setEndpointUrl(def.getProperties().getProperty("url", NewRelicClient.DEFAULT_ENDPOINT_URL));
		c.setCertificateValidationEnabled(Boolean.parseBoolean(def.getProperties().getProperty("certificateValidationEnabled", "true")));
		return c;
	}



}

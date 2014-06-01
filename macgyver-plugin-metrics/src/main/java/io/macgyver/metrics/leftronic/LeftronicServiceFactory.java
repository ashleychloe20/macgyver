package io.macgyver.metrics.leftronic;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import org.springframework.stereotype.Component;

import com.ning.http.client.AsyncHttpClient;

@Component
public class LeftronicServiceFactory extends BasicServiceFactory<Leftronic> {

	public LeftronicServiceFactory() {
		super("leftronic");

	}

	
	@Override
	public Leftronic doCreateInstance(ServiceDefinition def) {

		Leftronic leftronic = new Leftronic(new AsyncHttpClient());
		leftronic.setApiKey(def.getProperties().getProperty("apiKey"));
		leftronic.setPrefix(def.getProperties().getProperty("prefix"));
		return leftronic;

	}


	@Override
	public void doConfigureDefinition(ServiceDefinition def) {
		def.setLazyInit(false);
	}

}

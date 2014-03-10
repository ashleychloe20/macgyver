package io.macgyver.metrics.leftronic;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import org.springframework.stereotype.Component;

@Component
public class LeftronicServiceFactory extends BasicServiceFactory<Leftronic> {

	public LeftronicServiceFactory() {
		super("leftronic");

	}

	@Override
	public Leftronic doCreateInstance(ServiceDefinition def) {

		Leftronic leftronic = new Leftronic();
		leftronic.setApiKey(def.getProperties().getProperty("apiKey"));
		leftronic.setPrefix(def.getProperties().getProperty("prefix"));
		return leftronic;

	}

}

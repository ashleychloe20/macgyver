package io.macgyver.metrics.leftronic;

import java.util.Properties;

import org.springframework.stereotype.Component;

import io.macgyver.core.factory.ServiceFactory;

@Component
public class LeftronicServiceFactory extends ServiceFactory<Leftronic> {

	public LeftronicServiceFactory() {
		super("leftronic");

	}

	@Override
	public Leftronic createObject(Properties props)  {
		
		Leftronic leftronic = new Leftronic();
		leftronic.setApiKey(props.getProperty("apiKey"));
		leftronic.setPrefix(props.getProperty("prefix"));
		return leftronic;
		
	}

}

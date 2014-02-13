package io.macgyver.config;

import io.macgyver.core.ServiceFactoryBean;
import io.macgyver.metrics.leftronic.Leftronic;

public class LeftronicFactoryBean extends ServiceFactoryBean<Leftronic> {

	public LeftronicFactoryBean() {
		super(Leftronic.class);

	}

	@Override
	public Leftronic createObject() throws Exception {
		
		Leftronic leftronic = new Leftronic();
		leftronic.setApiKey(getProperties().getProperty("apiKey"));
		leftronic.setApiKey(getProperties().getProperty("prefix"));
		return leftronic;
		
	}

}

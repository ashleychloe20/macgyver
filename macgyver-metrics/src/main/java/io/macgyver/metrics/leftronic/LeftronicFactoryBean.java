package io.macgyver.metrics.leftronic;

import io.macgyver.core.ServiceFactoryBean;

public class LeftronicFactoryBean extends ServiceFactoryBean<Leftronic> {

	public LeftronicFactoryBean() {
		super(Leftronic.class);

	}

	@Override
	public Leftronic createObject() throws Exception {
		
		Leftronic leftronic = new Leftronic();
		leftronic.setApiKey(getProperties().getProperty("apiKey"));
		leftronic.setPrefix(getProperties().getProperty("prefix"));
		return leftronic;
		
	}

}

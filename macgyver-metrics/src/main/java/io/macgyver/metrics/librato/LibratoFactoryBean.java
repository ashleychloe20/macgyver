package io.macgyver.metrics.librato;

import com.ning.http.client.AsyncHttpClient;

import io.macgyver.core.ServiceFactoryBean;

public class LibratoFactoryBean extends ServiceFactoryBean<Librato> {

	public LibratoFactoryBean() {
		super(Librato.class);

	}

	@Override
	public Librato createObject() throws Exception {
		return new Librato(getProperties().getProperty("username"),
				getProperties().getProperty("apiKey"), new AsyncHttpClient(),
				getProperties().getProperty("prefix"));
	}

}

package io.macgyver.metrics.librato;

import io.macgyver.core.factory.ServiceFactory;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.ning.http.client.AsyncHttpClient;

@Component
public class LibratoServiceFactory extends ServiceFactory<Librato> {

	public LibratoServiceFactory() {
		super("librato");

	}

	@Override
	public Librato createObject(Properties props)  {
		return new Librato(props.getProperty("username"),
				props.getProperty("apiKey"), new AsyncHttpClient(),
				props.getProperty("prefix"));
	}

}

package io.macgyver.metrics.librato;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.ning.http.client.AsyncHttpClient;

@Component
public class LibratoServiceFactory extends BasicServiceFactory<Librato> {

	public LibratoServiceFactory() {
		super("librato");

	}

	@Override
	public Librato doCreateInstance(ServiceDefinition def)  {
		return new Librato(def.getProperties().getProperty("username"),
				def.getProperties().getProperty("apiKey"), new AsyncHttpClient(),
				def.getProperties().getProperty("prefix"));
	}

}

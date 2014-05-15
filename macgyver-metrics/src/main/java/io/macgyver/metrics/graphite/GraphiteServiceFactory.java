package io.macgyver.metrics.graphite;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.ning.http.client.AsyncHttpClient;

@Component
public class GraphiteServiceFactory extends BasicServiceFactory<Graphite> {

	public GraphiteServiceFactory() {
		super("graphite");
	}

	@Override
	public Graphite doCreateInstance(ServiceDefinition def)  {
		
		Properties props = def.getProperties();
		
		Graphite hg = new Graphite();
		hg.setPrefix(props.getProperty("prefix"));
		hg.setHost(props.getProperty("host"));
		hg.setPort(Integer.parseInt(props.getProperty("port")));

		return hg;
	}

	@Override
	public void doConfigureDefinition(ServiceDefinition def) {
		def.setLazyInit(false);
	}
	
	
}

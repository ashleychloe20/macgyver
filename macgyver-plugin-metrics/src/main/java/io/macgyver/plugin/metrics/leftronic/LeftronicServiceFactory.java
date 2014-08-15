package io.macgyver.plugin.metrics.leftronic;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import io.macgyver.core.Kernel;
import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Strings;
import com.ning.http.client.AsyncHttpClient;

@Component
public class LeftronicServiceFactory extends BasicServiceFactory<LeftronicReporter> {

	public LeftronicServiceFactory() {
		super("leftronic");

	}

	
	@Override
	public LeftronicReporter doCreateInstance(ServiceDefinition def) {
	Properties props = def.getProperties();
		
		MetricRegistry registry = Kernel.getInstance().getApplicationContext().getBean(MetricRegistry.class);
	
		
		LeftronicSender leftronic = new LeftronicSender(new AsyncHttpClient());
		leftronic.setApiKey(def.getProperties().getProperty("apiKey"));
		
		
		LeftronicReporter.Builder b = LeftronicReporter.forRegistry(registry);
		
		String prefix = props.getProperty("prefix");
		if (!Strings.isNullOrEmpty(prefix)) {
			b = b.prefixedWith(prefix);
		}
		
		String rateTimeUnit = props.getProperty("rateTimeUnit","MINUTES");
		b = b.convertRatesTo(TimeUnit.valueOf(rateTimeUnit));
		
		String durationTimeUnit = props.getProperty("durationTimeUnit","MILLISECONDS");
		b = b.convertDurationsTo(TimeUnit.valueOf(durationTimeUnit));
		
		
		LeftronicReporter r = b.build(leftronic);
		
		String reportInterval = props.getProperty("reportInterval","60");
		String reportIntervalTimeUnit = props.getProperty("reportIntervalTimeUnit",TimeUnit.SECONDS.toString());
		r.start(Integer.parseInt(reportInterval), TimeUnit.valueOf(reportIntervalTimeUnit));
	
		
		return r;

	}


	@Override
	public void doConfigureDefinition(ServiceDefinition def) {
		def.setLazyInit(false);
	}

}

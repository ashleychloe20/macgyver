package io.macgyver.plugin.metrics.graphite;

import io.macgyver.core.Kernel;
import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteReporter.Builder;
import com.google.common.base.Strings;

@Component
public class GraphiteReporterServiceFactory extends BasicServiceFactory<GraphiteReporter> {

	public GraphiteReporterServiceFactory() {
		super("graphite");
	}

	@Override
	public GraphiteReporter doCreateInstance(ServiceDefinition def)  {
		
		Properties props = def.getProperties();
		
		MetricRegistry registry = Kernel.getInstance().getApplicationContext().getBean(MetricRegistry.class);
		
		String host = props.getProperty("host");
		int port = Integer.parseInt(props.getProperty("port", "2003"));


		com.codahale.metrics.graphite.Graphite g = new com.codahale.metrics.graphite.Graphite(new InetSocketAddress(host,port));
		
		Builder b = GraphiteReporter.forRegistry(registry);
		
		String prefix = props.getProperty("prefix");
		if (!Strings.isNullOrEmpty(prefix)) {
			b = b.prefixedWith(prefix);
		}
		
		String rateTimeUnit = props.getProperty("rateTimeUnit","MINUTES");
		b = b.convertRatesTo(TimeUnit.valueOf(rateTimeUnit));
		
		String durationTimeUnit = props.getProperty("durationTimeUnit","MILLISECONDS");
		b = b.convertDurationsTo(TimeUnit.valueOf(durationTimeUnit));
		
		GraphiteReporter r = b.build(g);
		
		
		String reportInterval = props.getProperty("reportInterval","10");
		String reportIntervalTimeUnit = props.getProperty("reportIntervalTimeUnit","SECONDS");
		r.start(Integer.parseInt(reportInterval), TimeUnit.valueOf(reportIntervalTimeUnit));
	
		return r;
		
	}
	
	

	@Override
	public void doConfigureDefinition(ServiceDefinition def) {
		def.setLazyInit(false);
	}
	
	
}

package io.macgyver.plugin.metrics;

import io.macgyver.core.Kernel;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

public class Metrics {

	
	public static void gauge(String name, Number val) {
		MetricRegistry r = Kernel.getInstance().getApplicationContext().getBean("macMetricRegistry",MetricRegistry.class);

		Gauge g = r.getGauges().get(name);
		
		if (g==null) {
			NumberGauge gauge = new NumberGauge();
			r.register(name, gauge);
			gauge.setValue(val);
		}
		else {
			if (g instanceof NumberGauge) {
				NumberGauge ng = (NumberGauge) g;
				ng.setValue(val);
			}
			
			
		}
		
		
	}

}

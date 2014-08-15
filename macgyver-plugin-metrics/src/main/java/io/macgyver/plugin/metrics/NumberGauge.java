package io.macgyver.plugin.metrics;

import java.util.concurrent.atomic.AtomicReference;

import com.codahale.metrics.Gauge;

public class NumberGauge implements Gauge<Number> {

	AtomicReference<Number> val = new AtomicReference<>();

	public void setValue(Number val) {
		this.val.set(val);
	}
	
	@Override
	public Number getValue() {
		return val.get();
	
	}

}

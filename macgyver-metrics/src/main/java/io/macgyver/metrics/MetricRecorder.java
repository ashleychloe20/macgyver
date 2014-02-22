package io.macgyver.metrics;

public interface MetricRecorder {

	
	public void record(String name, long value);
}

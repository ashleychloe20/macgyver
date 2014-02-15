package io.macgyver.metrics;

public interface Recorder {

	public void record(String name, long value);
}

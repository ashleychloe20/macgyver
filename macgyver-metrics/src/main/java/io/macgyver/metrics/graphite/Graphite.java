package io.macgyver.metrics.graphite;

public abstract class Graphite {

	public abstract void send(String metric, long val);
}

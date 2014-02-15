package io.macgyver.metrics.graphite;

import io.macgyver.metrics.Recorder;

public abstract class Graphite implements Recorder {

	public abstract void record(String metric, long val);
}

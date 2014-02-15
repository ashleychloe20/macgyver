package io.macgyver.metrics.graphite;

public class OnPremGraphite extends Graphite {

	@Override
	public void record(String metric, long val) {
		throw new UnsupportedOperationException();

	}

}

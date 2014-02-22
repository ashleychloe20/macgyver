package io.macgyver.metrics.graphite;

public class OnPremGraphite extends Graphite {

	@Override
	public void doRecord(String metric, long val) {
		throw new UnsupportedOperationException();

	}

}

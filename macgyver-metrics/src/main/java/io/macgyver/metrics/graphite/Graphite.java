package io.macgyver.metrics.graphite;



public class Graphite extends AbstractGraphite {

	
	String host="localhost";
	int port = 2003;

	SimpleGraphiteClient simpleClient;

	SimpleGraphiteClient getSimpleGraphiteClient() {
		if (simpleClient == null) {
			simpleClient = new SimpleGraphiteClient(host, port);
		}
		return simpleClient;
	}

	@Override
	public void doRecord(String metric, Number val) {
		getSimpleGraphiteClient().sendMetric(metric, val.longValue());
	}

	public String getHost() {
		
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


}

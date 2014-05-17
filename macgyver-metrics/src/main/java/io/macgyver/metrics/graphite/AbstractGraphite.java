package io.macgyver.metrics.graphite;

import io.macgyver.core.jaxrs.GsonMessageBodyHandler;
import io.macgyver.metrics.AbstractMetricRecorder;
import io.macgyver.metrics.BasicTSV;
import io.macgyver.metrics.TSV;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncHttpClient;

public abstract class AbstractGraphite extends AbstractMetricRecorder {

	private String graphiteQueryBaseUrl;

	private Client client;
	protected AsyncHttpClient asyncClient;

	public AbstractGraphite() {

	}

	public AbstractGraphite(AsyncHttpClient client) {

		Preconditions.checkNotNull(client);

		this.asyncClient = client;
	}

	/**
	 * Returns the URL for making "REST" calls to graphite up to /graphite
	 * 
	 * @return
	 */
	public String getQueryBaseUrl() {
		return graphiteQueryBaseUrl;
	}

	public void setQueryBaseUrl(String url) {
		this.graphiteQueryBaseUrl = url;
	}



	public Iterable<TSV> queryTimeSeries(String target, String from) {
		return queryTimeSeries(target, from);
	}

	public Iterable<TSV> queryTimeSeries(String target) {
		return queryTimeSeries(target, null, null);
	}

	public Iterable<TSV> queryTimeSeries(String target, String from,
			String until) {
		Preconditions.checkNotNull(target);
		Map<String, String> m = Maps.newHashMap();
		m.put("target", target);
		if (!Strings.isNullOrEmpty(from)) {
			m.put("from", from);
		}

		if (!Strings.isNullOrEmpty(until)) {
			m.put("until", until);
		}

		JsonArray x = queryJsonArray(m);
		List<TSV> vals = Lists.newArrayList();

		for (int i = 0; i < x.size(); i++) {
			JsonObject obj = x.get(i).getAsJsonObject();
			String targetName = obj.get("target").getAsString();
			if (target.equals(targetName)) {
				JsonArray datapoints = obj.get("datapoints").getAsJsonArray();
				for (int j = 0; j < datapoints.size(); j++) {

					JsonArray tuple = datapoints.get(j).getAsJsonArray();
					JsonElement val = tuple.get(0);

					long timestamp = tuple.get(1).getAsLong();
					if (!val.isJsonNull()) {
						BasicTSV tsv = new BasicTSV(new DateTime(
								timestamp * 1000), val.getAsBigDecimal());
						vals.add(tsv);
					}
				}
			}
		}
		return vals;
	}

	public Optional<TSV> queryMostRecentValue(String target) {
		return queryMostRecentValue(target, null, null);
	}

	public Optional<TSV> queryMostRecentValue(String target, String from,
			String until) {
		try {
		Iterable<TSV> t = queryTimeSeries(target, from, until);
		if (t==null) {
			return Optional.absent();
		}
		return Optional.fromNullable(Iterables.getLast(t));
		}
		catch (NoSuchElementException e) {
			return Optional.absent();
		}
	}

	protected JsonArray queryJsonArray(Map<String, String> m) {
		Preconditions.checkNotNull(m);
		m.put("format", "json");

		WebTarget target = ClientBuilder.newClient().register(GsonMessageBodyHandler.class).target(getQueryBaseUrl()).path(
				"render");
		for (Map.Entry<String, String> entry : m.entrySet()) {
			target = target.queryParam(entry.getKey(), entry.getValue());
		}

		return target.request().get(JsonArray.class);
	}
}

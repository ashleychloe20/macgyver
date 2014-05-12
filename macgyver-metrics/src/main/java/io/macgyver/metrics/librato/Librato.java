package io.macgyver.metrics.librato;

import java.io.IOException;

import javax.json.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import io.macgyver.core.MacGyverException;
import io.macgyver.metrics.AbstractMetricRecorder;
import io.macgyver.metrics.MetricRecorder;

public class Librato extends AbstractMetricRecorder {

	String URL = "https://metrics-api.librato.com/v1/metrics";
	Logger logger = LoggerFactory.getLogger(Librato.class);
	AsyncHttpClient client;
	Gson gson = new Gson();

	String username;
	String apiKey;

	public Librato(String username, String apiKey, AsyncHttpClient client,
			String prefix) {
		Preconditions.checkNotNull(client, "client cannot be null");
		this.client = client;
		this.username = username;
		this.apiKey = apiKey;
		setPrefix(prefix);
		if (Strings.isNullOrEmpty(username)) {
			logger.warn("username not set");
		}
		if (Strings.isNullOrEmpty(apiKey)) {
			logger.warn("apiKey not set");
		}
	}

	@Override
	public void doRecord(String name, Number val) {

		JsonObject payload = new JsonObject();
		JsonObject gauges = new JsonObject();
		payload.add("gauges", gauges);
		JsonObject value = new JsonObject();
		value.addProperty("value", val.longValue());



		name = name.replace(" ", "_");
		gauges.add(name, value);

		sendMetric(payload);
	}

	protected void sendMetric(JsonObject jsonData) {
		try {

			String data = jsonData.toString();

			AsyncCompletionHandler<String> h = new AsyncCompletionHandler<String>() {

				@Override
				public void onThrowable(Throwable t) {
					logger.warn("", t);

				}

				@Override
				public String onCompleted(Response response) throws Exception {
					
					if (response.getStatusCode() > 299) {
						logger.warn("librato response code: {} body: {}",
								response.getStatusCode(),
								response.getResponseBody());
					}

					return null;
				}
			};
			
			client.preparePost(URL)
					.addHeader(
							"Authorization",
							"Basic "
									+ BaseEncoding.base64().encode(
											(username + ":" + apiKey)
													.getBytes()))
					.addHeader("content-type", "application/json")
					.setBody(data).execute(h);

		} catch (IOException e) {
			throw new MacGyverException(e);
		} finally {

		}

	}

}

package io.macgyver.metrics.leftronic;

import io.macgyver.core.MacGyverException;
import io.macgyver.metrics.AbstractMetricRecorder;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class Leftronic extends AbstractMetricRecorder {

	public static Logger logger = LoggerFactory.getLogger(Leftronic.class);
	public static final String DEFAULT_URL = "https://www.leftronic.com/customSend/";
	String url = DEFAULT_URL;
	String apiKey;
	
	AsyncHttpClient client;
	
	public Leftronic(AsyncHttpClient client) {
		Preconditions.checkNotNull(client);
		this.client = client;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void doRecord(String streamName, Number val) {

		try {
			Gson gson = new Gson();

			JsonObject data = new JsonObject();
			data.addProperty("accessKey", apiKey);
			data.addProperty("streamName", streamName);
			data.addProperty("point", val.longValue());

			if (logger.isTraceEnabled()) {
				logger.trace("sending data leftronic: {}", data);
			}

			AsyncCompletionHandler<String> h = new AsyncCompletionHandler<String>() {

				@Override
				public void onThrowable(Throwable t) {
					logger.warn("", t);
					super.onThrowable(t);

				}

				@Override
				public String onCompleted(Response response) throws Exception {
					int sc = response.getStatusCode();
					if (sc >= 300) {
						logger.warn("leftronic response code: {} body: {}", sc,response.getResponseBody());
					}
					return null;
				}
			};

			client.preparePost(url).setBody(data.toString()).execute(h);

		} catch (IOException e) {
			throw new MacGyverException(e);
		} finally {

		}

	}

}

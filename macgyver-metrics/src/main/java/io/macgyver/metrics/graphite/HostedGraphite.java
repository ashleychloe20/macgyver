package io.macgyver.metrics.graphite;

import io.macgyver.core.MacGyverException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class HostedGraphite extends Graphite {

	Logger logger = LoggerFactory.getLogger(HostedGraphite.class);

	String url = "https://hostedgraphite.com/api/v1/sink";
	String accessKey = "";
	String prefix;
	@Autowired
	AsyncHttpClient client;

	public String getApiKey() {
		return accessKey;
	}

	public void setApiKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String qualifyStreamName(String stream) {
		if (!Strings.isNullOrEmpty(prefix)) {
			return prefix + "." + stream;
		} else {
			return stream;
		}
	}

	@Override
	public void record(String metric, long val) {
		
		try {

			String data = qualifyStreamName(metric) + " " + val;

			AsyncCompletionHandler<String> h = new AsyncCompletionHandler<String>() {

				@Override
				public void onThrowable(Throwable t) {
					logger.warn("", t);
					super.onThrowable(t);

				}

				@Override
				public String onCompleted(Response response) throws Exception {
					logger.debug("sent metric to graphite");
					return null;
				}
			};

			client.preparePost(url)
					.addHeader(
							"Authorization",
							"Basic "
									+ BaseEncoding.base64().encode(
											accessKey.getBytes()))
					.setBody(data).execute(h);

		} catch (IOException e) {
			throw new MacGyverException(e);
		} finally {

		}

	}

}

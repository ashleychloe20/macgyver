package io.macgyver.metrics.graphite;

import io.macgyver.core.MacGyverException;

import java.io.IOException;

import org.glassfish.jersey.client.ClientConfig;
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

	
	public HostedGraphite() {
		// TODO Auto-generated constructor stub
	}
	
	public HostedGraphite(ClientConfig cc) {
		super(cc);
	}
	public String getApiKey() {
		return accessKey;
	}

	public void setApiKey(String accessKey) {
		this.accessKey = accessKey == null ? "" : accessKey;
	}




	@Override
	public void doRecord(String metric, long val) {

		try {

			String data = metric + " " + val;

			AsyncCompletionHandler<String> h = new AsyncCompletionHandler<String>() {

				@Override
				public void onThrowable(Throwable t) {
					logger.warn("", t);
					super.onThrowable(t);

				}

				@Override
				public String onCompleted(Response response) throws Exception {
					int rc = response.getStatusCode();

					if (logger.isDebugEnabled()) {
						logger.debug("sent metric to graphite rc={}", rc);
					}
					if (rc > 299) {
						logger.warn("graphite response: rc={} body={}", rc,
								response.getResponseBody());
					}
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

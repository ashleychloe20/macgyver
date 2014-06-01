package io.macgyver.metrics.graphite;

import io.macgyver.core.MacGyverException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class HostedGraphite extends AbstractGraphite {

	Logger logger = LoggerFactory.getLogger(HostedGraphite.class);

	String url = "https://hostedgraphite.com/api/v1/sink";
	String accessKey = "";
	String prefix;

	public HostedGraphite(AsyncHttpClient client) {
		super(client);
	}

	public String getApiKey() {
		return accessKey;
	}

	public void setApiKey(String accessKey) {
		this.accessKey = accessKey == null ? "" : accessKey;
	}

	@Override
	public void doRecord(String metric, Number val) {
		Preconditions.checkNotNull(asyncClient, "client must be initialized");
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

					if (rc > 299) {
						logger.warn("graphite response: rc={} body={}", rc,
								response.getResponseBody());
					}
					return null;
				}
			};

			asyncClient
					.preparePost(url)
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

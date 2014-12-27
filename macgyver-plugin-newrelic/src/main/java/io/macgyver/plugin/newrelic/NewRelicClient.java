/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.plugin.newrelic;

import io.macgyver.core.ServiceInvocationException;
import io.macgyver.core.jaxrs.SslTrust;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jboss.resteasy.plugins.providers.FormUrlEncodedProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class NewRelicClient {

	public static final String DEFAULT_ENDPOINT_URL = "https://api.newrelic.com/";
	protected String endpointUrl = DEFAULT_ENDPOINT_URL;
	protected String apiKey;
	private OkHttpClient client;
	private boolean validateCertificates = false;
	ObjectMapper mapper = new ObjectMapper();

	public void setApiKey(String key) {
		this.apiKey = key;
	}

	public synchronized OkHttpClient getClient() {
		if (client == null) {
			client = newClient();
		}
		return client;
	}

	protected OkHttpClient newClient() {

		OkHttpClient c = new OkHttpClient();
		c.setConnectTimeout(20, TimeUnit.SECONDS);

		return c;
	}

	public ObjectNode get(String x, String... args) {

		if (args.length > 0) {
			FormEncodingBuilder b = new FormEncodingBuilder();

			if (args.length % 2 != 0) {
				throw new IllegalArgumentException(
						"must be an even number of arguments");
			}
			for (int i = 0; i < args.length; i += 2) {
				b = b.add(args[i], args[i + 1]);

			}
			return get(x, b.build());
		} else {
			return get(x,
					new FormEncodingBuilder().add("__dummy__", "__dummy__")
							.build());
		}

	}

	protected void throwExceptionIfNecessary(Response r) {
		try {
			if (r.code() >= 400) {
				ObjectNode n = (ObjectNode) mapper.readTree(r.body().string());

				throw new ServiceInvocationException(n.path("error")
						.path("title").asText());
			}
		} catch (IOException e) {
			throw new ServiceInvocationException(e);
		}
	}

	public ObjectNode get(String x, RequestBody form) {

		try {
			Request request = new Request.Builder()
					.url(getEndpointUrl() + "/v2/" + x)
					.addHeader(
							"Content-type",
							org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
					.addHeader(
							"Accept",
							org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
					.addHeader("X-Api-Key", apiKey).post(form).build();
			Response response = getClient().newCall(request).execute();

			throwExceptionIfNecessary(response);
			ObjectNode n = (ObjectNode) mapper.readTree(response.body()
					.string());

			return n;
		} catch (IOException e) {
			throw new ServiceInvocationException(e);
		}
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String url) {
		this.endpointUrl = url;
	}

	public boolean getCertificateValidationEnabled() {
		return this.validateCertificates;
	}

	public void setCertificateValidationEnabled(boolean validationEnabled) {
		this.validateCertificates = validationEnabled;
	}
}

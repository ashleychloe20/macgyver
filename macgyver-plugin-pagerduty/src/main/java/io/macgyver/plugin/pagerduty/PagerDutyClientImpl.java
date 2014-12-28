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
package io.macgyver.plugin.pagerduty;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.jaxrs.SslTrust;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class PagerDutyClientImpl implements PagerDutyClient {

	public static final String DEFAULT_EVENTS_ENDPOINT_URL = "https://events.pagerduty.com/generic/2010-04-15";
	protected String eventsEndpointUrl = DEFAULT_EVENTS_ENDPOINT_URL;

	private OkHttpClient client;
	private boolean validateCertificates = false;

	private String serviceKey;
	ObjectMapper mapper = new ObjectMapper();

	public synchronized OkHttpClient getEventClient() {
		if (client == null) {
			client = newEventClient();
		}
		return client;
	}

	protected OkHttpClient newEventClient() {
		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(10, TimeUnit.SECONDS);

		if (!getCertificateValidationEnabled()) {
			client = client.setHostnameVerifier(SslTrust
					.withoutHostnameVerification());
			client = client.setSslSocketFactory(SslTrust
					.withoutCertificateValidation().getSocketFactory());

		}

		return client;
	}

	public ObjectNode postEvent(ObjectNode input) {

		try {
			Request r = new Request.Builder()
					.url(eventsEndpointUrl + "/create_event.json")
					.post(RequestBody.create(
							MediaType.parse("application/json"),
							input.toString()))
					.addHeader(
							"Accept",
							org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
					.build();

			Response response = getEventClient().newCall(r).execute();

			ObjectNode rv = (ObjectNode) mapper.readTree(response.body()
					.string());

			throwExceptionIfNecessary(rv);

			return rv;
		} catch (IOException e) {
			throw new MacGyverException(e);
		}
	}

	public String getEventsEndpointUrl() {
		return eventsEndpointUrl;
	}

	public void setEventsEndpointUrl(String url) {
		this.eventsEndpointUrl = url;
	}

	public boolean getCertificateValidationEnabled() {
		return this.validateCertificates;
	}

	public void setCertificateValidationEnabled(boolean validationEnabled) {
		this.validateCertificates = validationEnabled;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String key) {
		this.serviceKey = key;
	}

	public ObjectNode createIncident(String incidentKey, String description) {
		return createIncident(incidentKey, description, null, null, null);
	}

	protected ObjectNode formatRequest(String operation, String incidentKey,
			String description, String client, String clientUrl, ObjectNode n) {
		String serviceKey = getServiceKey();
		if (Strings.isNullOrEmpty(serviceKey)) {
			throw new MacGyverException("serviceKey must be set");
		}

		ObjectNode input = new ObjectMapper().createObjectNode();
		input.put("service_key", getServiceKey());
		input.put("event_type", operation);
		input.put("description", description);

		if (!Strings.isNullOrEmpty(incidentKey)) {
			input.put("incident_key", incidentKey);
		}
		if (!Strings.isNullOrEmpty(client)) {
			input.put("client", client);
		}
		if (!Strings.isNullOrEmpty(clientUrl)) {
			input.put("client_url", clientUrl);
		}
		if (n != null) {
			input.set("details", n);
		}
		return input;
	}

	@Override
	public ObjectNode createIncident(String incidentKey, String description,
			String client, String clientUrl, ObjectNode n) {

		return postEvent(formatRequest("trigger", incidentKey, description,
				client, clientUrl, n));

	}
	
	void throwExceptionIfNecessary(ObjectNode n) {
		JsonNode errors = n.path("errors");
		if (errors.isArray()) {
			ArrayNode an = (ArrayNode) errors;
			if (an.size()>0) {
				throw new PagerDutyInvocationException(an.get(0).asText());
			}
		}
	}
	
}

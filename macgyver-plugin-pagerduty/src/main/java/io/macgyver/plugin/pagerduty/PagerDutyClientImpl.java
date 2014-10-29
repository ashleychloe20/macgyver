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

import io.macgyver.core.jaxrs.SslTrust;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class PagerDutyClientImpl implements PagerDutyClient {

	public static final String DEFAULT_EVENTS_ENDPOINT_URL = "https://events.pagerduty.com/generic/2010-04-15";
	protected String eventsEndpointUrl = DEFAULT_EVENTS_ENDPOINT_URL;

	private Client client;
	private boolean validateCertificates = false;

	private String serviceKey;

	public synchronized Client getEventClient() {
		if (client == null) {
			client = newEventClient();
		}
		return client;
	}

	protected Client newEventClient() {

		ClientBuilder builder = new ResteasyClientBuilder()
				.establishConnectionTimeout(10, TimeUnit.SECONDS);

		if (!getCertificateValidationEnabled()) {
			builder = builder.hostnameVerifier(
					SslTrust.withoutHostnameVerification()).sslContext(
					SslTrust.withoutCertificateValidation());
		}

		return builder.build();
	}

	public ObjectNode postEvent(ObjectNode input) {

		ObjectNode rv = (ObjectNode) eventsTarget().request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(input), JsonNode.class);

		return rv;
	}

	public WebTarget eventsTarget() {
		return newEventsWebTarget().path("create_event.json");
	}

	public WebTarget newEventsWebTarget() {
		WebTarget wt = getEventClient().target(getEventsEndpointUrl());

		return wt;

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
		return createIncident(incidentKey, description,null,null,null);
	}
	@Override
	public ObjectNode createIncident(String incidentKey, String description,
			String client, String clientUrl, ObjectNode n) {
		String serviceKey = getServiceKey();
		if (!Strings.isNullOrEmpty(serviceKey)) {
			throw new BadRequestException("serviceKey must be set");
		}
		
		ObjectNode input = new ObjectMapper().createObjectNode();
		input.put("service_key", getServiceKey());
		input.put("event_type", "trigger");
		input.put("description", description);

		if (!Strings.isNullOrEmpty(client)) {
			input.put("client", client);
		}
		if (!Strings.isNullOrEmpty(clientUrl)) {
			input.put("client_url", clientUrl);
		}
		if (n != null) {
			input.put("details", n);
		}
		return postEvent(input);

	}
}

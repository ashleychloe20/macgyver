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
package io.macgyver.plugin.splunk;

import io.macgyver.core.jaxrs.SslTrust;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SplunkClient {

	public static final String DEFAULT_ENDPOINT_URL="https://api.newrelic.com/";
	protected String endpointUrl=DEFAULT_ENDPOINT_URL;
	protected String apiKey;
	private Client client;
	private boolean validateCertificates=false;
	
	public void setApiKey(String key) {
		this.apiKey = key;
	}
	
	
	public synchronized Client getClient() {
		if (client==null) {
			client = newClient();
		}
		return client;
	}
	protected Client newClient() {

		ClientBuilder builder = new ResteasyClientBuilder()
				.establishConnectionTimeout(10, TimeUnit.SECONDS);
		builder.register(new ClientAuthFilter());
		if (!getCertificateValidationEnabled()) {
			builder = builder.hostnameVerifier(
					SslTrust.withoutHostnameVerification()).sslContext(
					SslTrust.withoutCertificateValidation());
		}

		return builder.build();
	}
	
	public ObjectNode get(String x, String...args) {
		
		Form f = new Form();
		if (args.length%2!=0) {
			throw new IllegalArgumentException("must be an even number of arguments");
		}
		for (int i=0; i<args.length; i+=2) {
			f.param(args[i], args[i+1]);
		}
		return get(x,f);
	}
	public ObjectNode get(String x, Form form) {
		
		ObjectNode rv = (ObjectNode) v2().path(x).request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),JsonNode.class);
		
		return rv;
	}
	public WebTarget v2() {
		return newWebTarget().path("v2");
	}
	
	public WebTarget newWebTarget() {
		WebTarget wt = getClient().target(getEndpointUrl());

		return wt;
		
	}
	
	public String getEndpointUrl() {
		return endpointUrl;
	}
	
	public void setEndpointUrl(String url) {
		this.endpointUrl = url;
	}
	public class ClientAuthFilter implements ClientRequestFilter {
		@Override
		public void filter(ClientRequestContext requestContext) throws IOException {

			requestContext.getHeaders().putSingle("X-Api-Key", apiKey);

		}
	}
	
	public boolean getCertificateValidationEnabled() {
		return this.validateCertificates;
	}
	public void setCertificateValidationEnabled(boolean validationEnabled) {
		this.validateCertificates=validationEnabled;
	}
}

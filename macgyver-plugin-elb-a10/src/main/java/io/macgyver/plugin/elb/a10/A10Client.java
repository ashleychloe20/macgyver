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
package io.macgyver.plugin.elb.a10;

import io.macgyver.core.jaxrs.SslTrust;
import io.macgyver.plugin.elb.ElbException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;


public class A10Client {

	public static final String A10_AUTH_TOKEN_KEY = "token";
	Logger logger = LoggerFactory.getLogger(A10Client.class);
	private String username;
	private String password;
	private String url;
	Cache<String, String> tokenCache;

	public static final int DEFAULT_TOKEN_CACHE_DURATION = 10;
	private static final TimeUnit DEFAULT_TOKEN_CACHE_DURATION_TIME_UNIT = TimeUnit.MINUTES;

	public boolean validateCertificates = true;


	public A10Client(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;

		setTokenCacheDuration(DEFAULT_TOKEN_CACHE_DURATION,
				DEFAULT_TOKEN_CACHE_DURATION_TIME_UNIT);

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTokenCacheDuration(int duration, TimeUnit timeUnit) {
		Preconditions.checkArgument(duration >= 0, "duration must be >=0");
		Preconditions.checkNotNull(timeUnit, "TimeUnit must be set");

		this.tokenCache = CacheBuilder.newBuilder()
				.expireAfterWrite(duration, timeUnit).build();

	}

	public void setCertificateVerificationEnabled(boolean b) {
		validateCertificates = b;
		if (validateCertificates && (!b)) {
			logger.warn("certificate validation disabled");
		}
	}

	void throwExceptionIfNecessary(ObjectNode response) {

		if (response.has("response") && response.get("response").has("err")) {
			
				String code = response.path("response").path("err").path("code").asText();
				String msg = response.path("response").path("err").path("msg").asText();
				
				logger.warn("error response: {}", response);
				A10RemoteException x = new A10RemoteException(code, msg);
				throw x;
			
		}

	}

	protected String authenticate() {
		WebTarget wt = newWebTarget();

		Form f = new Form().param("username", username)
				.param("password", password).param("format", "json")
				.param("method", "authenticate");
		Response resp = wt.request().post(
				Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		ObjectNode obj = resp
				.readEntity(ObjectNode.class);

		throwExceptionIfNecessary(obj);

		String sid = obj.path("session_id").asText();
		if (Strings.isNullOrEmpty(sid)) {
			throw new ElbException("authentication failed");
		}
		tokenCache.put(A10_AUTH_TOKEN_KEY, sid);
		return sid;

	}

	protected String getAuthToken() {
		String token = tokenCache.getIfPresent(A10_AUTH_TOKEN_KEY);
		if (token == null) {
			token = authenticate();
		}

		if (token == null) {
			throw new ElbException(
					"could not obtain auth token");
		}
		return token;

	}

	public ObjectNode invoke(String method) {
		Map<String,String> m = Maps.newHashMap();
		return invoke(method, m);
	}

	protected Map<String,String> toMap(String...args) {
		Map<String,String> m = Maps.newHashMap();
		if (args==null || args.length==0) {
			return m;
		}
		if (args.length %2 !=0) {
			throw new IllegalArgumentException("arguments must be in multiples of 2 (key/value)");
		}	
		for (int i=0; i<args.length; i+=2) {
			Preconditions.checkNotNull(args[i]);
			Preconditions.checkNotNull(args[i+1]);
			m.put(args[i], args[i+1]);
		}
		return m;
	}
	public ObjectNode invoke(String method, String...args) {
		return invoke(method,toMap(args));
	}
	public ObjectNode invoke(String method, Map<String, String> params) {
		if (params == null) {
			params = Maps.newConcurrentMap();
		}
		Map<String, String> copy = Maps.newHashMap(params);
		copy.put("method", method);

		return invoke(copy);
	}

	protected ObjectNode invoke(Map<String, String> x) {
		try {
			WebTarget wt = newWebTarget();

			String method = x.get("method");
			Preconditions.checkArgument(!Strings.isNullOrEmpty(method),
					"method argument must be passed");
			Form f = new Form().param("session_id", getAuthToken())
					.param("format", "json").param("method", method);

			Response resp = wt.request()
					.post(Entity.entity(f,
							MediaType.APPLICATION_FORM_URLENCODED_TYPE));

			String contentType = resp.getHeaderString("Content-Type");

			String rawResponse = resp.readEntity(String.class);

			ObjectNode response = (ObjectNode) new ObjectMapper()
					.readTree(rawResponse);
			
			ObjectMapper mapper = new ObjectMapper();
			String body = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(response);
			logger.debug("response: \n{}", body);
			throwExceptionIfNecessary(response);

			return response;
		} catch (IOException e) {
			throw new ElbException(e);
		}

	}

	public ObjectNode getDeviceInfo() {
		return invoke("system.device_info.get");
	}

	public ObjectNode getSystemInfo() {
		return invoke("system.information.get");
	}

	public ObjectNode getSystemPerformance() {
		return invoke("system.performance.get");
	}

	public ObjectNode getServiceGroupAll() {

		ObjectNode obj = invoke("slb.service_group.getAll");

		return obj;

	}

	protected Client newClient() {

		ClientBuilder builder = new ResteasyClientBuilder()
				.establishConnectionTimeout(10, TimeUnit.SECONDS);

		if (!validateCertificates) {
			builder = builder.hostnameVerifier(
					SslTrust.withoutHostnameVerification()).sslContext(
					SslTrust.withoutCertificateValidation());
		}
		return builder.build();
	}

	protected WebTarget newWebTarget() {

		return newClient().target(url);

	}
}

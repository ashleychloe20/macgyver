package io.macgyver.elb.a10;

import io.macgyver.core.jaxrs.GsonMessageBodyHandler;
import io.macgyver.core.jaxrs.SslTrust;
import io.macgyver.elb.ElbException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

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

	Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

	public A10Client(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;

		setTokenCacheDuration(DEFAULT_TOKEN_CACHE_DURATION, DEFAULT_TOKEN_CACHE_DURATION_TIME_UNIT);
		
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
		Preconditions.checkArgument(duration>=0, "duration must be >=0");
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

	void throwExceptionIfNecessary(JsonObject response) {

		if (response.has("response")) {
			JsonObject responseNode = response.get("response")
					.getAsJsonObject();
			if (responseNode.has("err")) {
				JsonObject err = responseNode.get("err").getAsJsonObject();
				String code = err.get("code").getAsString();
				String msg = err.get("msg").getAsString();
				logger.warn("error response: \n{}", new GsonBuilder()
						.setPrettyPrinting().create().toJson(response));
				A10RemoteException x = new A10RemoteException(code, msg);
				throw x;
			}
		}

	}

	protected String authenticate() {
		WebTarget wt = newWebTarget();

		Form f = new Form().param("username", username)
				.param("password", password).param("format", "json")
				.param("method", "authenticate");
		Response resp = wt.request().post(
				Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		com.google.gson.JsonObject obj = resp
				.readEntity(com.google.gson.JsonObject.class);

		throwExceptionIfNecessary(obj);

		String sid = obj.get("session_id").getAsString();
		if (sid == null) {
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
			throw new ElbException("could not obtain auth token");
		}
		return token;

	}

	public JsonObject exec(String method) {
		return exec(method, null);
	}

	public JsonObject exec(String method, Map<String, String> params) {
		if (params == null) {
			params = Maps.newConcurrentMap();
		}
		Map copy = Maps.newHashMap(params);
		copy.put("method", method);

		return exec(copy);
	}

	protected JsonObject exec(Map<String, String> x) {
		WebTarget wt = newWebTarget();

		String method = x.get("method");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(method),
				"method argument must be passed");
		Form f = new Form().param("session_id", getAuthToken())
				.param("format", "json").param("method", method);

		Response resp = wt.request().post(
				Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		com.google.gson.JsonObject obj = resp
				.readEntity(com.google.gson.JsonObject.class);
		logger.debug("response: \n{}", prettyGson.toJson(obj));
		return obj;
	}

	public JsonObject getAllSLB() {

		com.google.gson.JsonObject obj = exec("slb.service_group.getAll");

		return obj;

	}

	protected Client newClient() {
		ClientBuilder builder = ClientBuilder.newBuilder().register(
				new GsonMessageBodyHandler());

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

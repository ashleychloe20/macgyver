package io.macgyver.core.rest;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request.Builder;

/**
 * Simple interface for interacting with JSON REST services.  Intended for use with strong typing 
 * is not desired.
 * @author rschoening
 *
 */
public interface JsonRestClient {

	public static interface Injector {
		public Builder inject(Builder b);
	}
	
	public JsonNode get(String path);
	public JsonNode get(String path, Map<String, String> queryParameters);
	public JsonNode post(String path, JsonNode n);
	public Builder createBuilder(String path);
	public Response execute(Request request);
	public void addInjector(Injector j);
	public void addBasicAuthInjector(String username, String password);
}

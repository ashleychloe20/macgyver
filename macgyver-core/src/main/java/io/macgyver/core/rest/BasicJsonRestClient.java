package io.macgyver.core.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * JsonRestClient is a simple wrapper around OkHttp that makes it easy to work with JSON payloads.
 *
 * @author rschoening
 *
 */
public class BasicJsonRestClient implements JsonRestClient {

	Logger logger = LoggerFactory.getLogger(BasicJsonRestClient.class);
	protected OkHttpClient okHttpClient = new OkHttpClient();
	protected String urlRoot;

	protected ObjectMapper mapper = new ObjectMapper();

	List<Injector> injectors =  Lists.newCopyOnWriteArrayList();
	

	public static class BasicAuthInjector implements Injector {
		String username;
		String password;
		public BasicAuthInjector(String username, String password) {
			this.username = username;
			this.password=password;
		}
		@Override
		public Builder inject(Builder b) {
			return b.addHeader("Authorization",Credentials.basic(username, password));
			
		}
	}
	public BasicJsonRestClient(String urlRoot) {
		Preconditions.checkNotNull(urlRoot, "base url cannot be null");
		Preconditions.checkArgument(urlRoot.startsWith("http://") || urlRoot.startsWith("https://"),"base url must use http or https");
		this.urlRoot = urlRoot;
	}

	public String getUrlBase() {
		return urlRoot;
	}

	public void addInjector(Injector j) {
		injectors.add(j);
	}
	public void addBasicAuthInjector(String username, String password) {
		addInjector(new BasicAuthInjector(username, password));
	}
	public Response execute(Request request) {
		try {
			return this.okHttpClient.newCall(request).execute();
		} catch (IOException e) {
			throw new RestException(e);
		}
	}

	public Builder createBuilder(String path) {
		return createBuilder(path,null);
	}
	public Builder createBuilder(String path, Map<String,String> q) {
		return inject(new Request.Builder().url(new UrlBuilder()
				.base(urlRoot).path(path).queryParam(q).build()));
	}

	protected final Builder inject(Builder b) {
		
		for (Injector injector: injectors) {
			b = injector.inject(b);
		}
		return b;
	}

	public JsonNode post(String uri, JsonNode n) {

		try {

			Builder builder = createBuilder(uri).addHeader("accept",
					"application/json");
			builder = inject(builder);
			builder = builder.post(RequestBody.create(
					MediaType.parse("application/json"), n.toString()));
			Response response = execute(builder.build());
			if (!response.isSuccessful()) {
				throw new RestException(response.code());
			}

			return mapper.readTree(response.body().charStream());
		} catch (RestException e) {
			throw e;
		} catch (IOException | RuntimeException e) {
			throw new RestException(e);
		}

	}

	public JsonNode get(String path) {
		return get(path, null);
	}

	public JsonNode get(String path, Map<String, String> queryParameters) {

		try {
		
			Builder builder = createBuilder(path, queryParameters).addHeader("accept",
					"application/json");
			
			Response response = execute(builder.build());
			if (!response.isSuccessful()) {
				throw new RestException(response.code());
			}

			return mapper.readTree(response.body().charStream());
		} catch (RestException e) {
			throw e;
		} catch (IOException | RuntimeException e) {
			throw new RestException(e);
		}

	}
}

package io.macgyver.neo4j.rest;

import io.macgyver.neo4j.rest.impl.NonStreamingResultSetImpl;
import io.macgyver.neo4j.rest.impl.SslTrust;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Neo4jRestClient {
	
	Logger logger = LoggerFactory.getLogger(Neo4jRestClient.class);
	
	public static final String DEFAULT_URL = "http://localhost:7474";
	String url = DEFAULT_URL;
	String username;
	String password;
	boolean validateCertificates = false;
	boolean streamResponse = true;
	ObjectMapper mapper = new ObjectMapper();

	public Neo4jRestClient() {
		this(DEFAULT_URL);
	}

	public Neo4jRestClient(String url) {
		Preconditions.checkNotNull(url);
		this.url = url;
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

	public ObjectNode createParameters(Object... args) {
		Preconditions.checkNotNull(args);
		Preconditions.checkArgument(args.length % 2 == 0,
				"must be an even number of arguments (key/value pairs)");
		ObjectNode n = mapper.createObjectNode();
		for (int i = 0; i < args.length; i += 2) {
			String key = args[i].toString();

			Object val = args[i + 1];
			if (val == null) {
				n.put(key, NullNode.getInstance());
			} else if (val instanceof String) {
				n.put(key, val.toString());
			} else if (val instanceof Integer) {
				n.put(key, (Integer) val);
			} else if (val instanceof Long) {
				n.put(key, (Long) val);
			} else if (val instanceof Double) {
				n.put(key, (Double) val);
			} else if (val instanceof Boolean) {
				n.put(key, (Boolean) val);
			} else if (val instanceof List) {
				List x = Lists.newArrayList();
				ArrayNode an = mapper.createArrayNode();

				for (Object item : (List) val) {
					an.add(item.toString());
				}
				n.put(key, an);
			} else {
				throw new IllegalArgumentException("type for param " + key
						+ " not supported: " + val.getClass().getName());
			}

		}
		return n;
	}

	public Result execCypher(String cypher, Object... args) {
		return execCypher(cypher, createParameters(args));
	}

	public Result execCypher(String cypher) {
		return execCypher(cypher, mapper.createObjectNode());
	}

	public Result execCypher(String cypher, ObjectNode params) {
		ObjectNode n = execCypherWithJsonResponse(cypher, params);
		return new NonStreamingResultSetImpl(n);
	}

	public ObjectNode execCypherWithJsonResponse(String cypher) {
		return execCypherWithJsonResponse(cypher, mapper.createObjectNode());
	}

	public ObjectNode execCypherWithJsonResponse(String cypher,
			ObjectNode params) {
		WebTarget wt = newWebTarget();

		wt = wt.path("db/data").path("cypher");
		ObjectNode payload = new ObjectMapper().createObjectNode();
		payload.put("query", cypher);
		payload.put("params", params);

		Response response = wt.request().accept(MediaType.APPLICATION_JSON)
				.header("X-Stream", Boolean.toString(streamResponse))
				.buildPost(Entity.entity(payload, MediaType.APPLICATION_JSON))
				.invoke();

		int sc = response.getStatus();
		if (sc != 200) {
			ObjectNode x = response.readEntity(ObjectNode.class);
			throw new Neo4jException(x.path("message").asText());

		}

		return (ObjectNode) response.readEntity(ObjectNode.class);
	}

	public boolean isValidateCertificates() {
		return validateCertificates;
	}

	public void setValidateCertificates(boolean validateCertificates) {
		this.validateCertificates = validateCertificates;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean checkOnline() {
		try {
			Response r = newWebTarget().path("/").request().accept(MediaType.APPLICATION_JSON)
					.buildGet().invoke();

			if (r.getStatus() == 200) {
				return true;
			}
			else {
				logger.warn("neo4j health check returned sc={}",r.getStatus());
			}
		} catch (Exception e) {
			logger.warn("could not communicate with neo4j",e);
			
		}
		return false;
	}

}

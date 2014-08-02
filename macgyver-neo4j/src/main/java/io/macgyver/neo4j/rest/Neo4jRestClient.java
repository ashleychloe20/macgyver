package io.macgyver.neo4j.rest;

import io.macgyver.neo4j.rest.impl.ResultSetImpl;


import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Neo4jRestClient {
	String url = "http://localhost:7474/db/data";
	String username;
	String password;
	boolean validateCertificates=false;
	boolean streamResponse=true;
	ObjectMapper mapper = new ObjectMapper();
	
	protected Client newClient() {
		
		ClientBuilder builder = new ResteasyClientBuilder()
				.establishConnectionTimeout(10, TimeUnit.SECONDS);
/*
		if (!validateCertificates) {
			builder = builder.hostnameVerifier(
					SslTrust.withoutHostnameVerification()).sslContext(
					SslTrust.withoutCertificateValidation());
		}
		*/
		return builder.build();
	}

	protected WebTarget newWebTarget() {

		return newClient().target(url);

	}
	public Result execCypher(String cypher) {
		return execCypher(cypher, mapper.createObjectNode());
	}
	public Result execCypher(String cypher, ObjectNode params) {
		ObjectNode n = execCypherWithJsonResponse(cypher,params);
		return new ResultSetImpl(n);
	}
	public ObjectNode execCypherWithJsonResponse(String cypher) {
		return execCypherWithJsonResponse(cypher, mapper.createObjectNode());
	}
	
	public ObjectNode execCypherWithJsonResponse(String cypher, ObjectNode params) {
		WebTarget wt = newWebTarget();
		
		wt = wt.path("cypher");
		ObjectNode payload = new ObjectMapper().createObjectNode();
		payload.put("query", cypher);
		payload.put("params",params);

		Response response = wt.request().accept(MediaType.APPLICATION_JSON).header("X-Stream", Boolean.toString(streamResponse)). buildPost( Entity.entity(payload, MediaType.APPLICATION_JSON)).invoke();
		
	
		int sc = response.getStatus();
		if (sc!=200) {
			ObjectNode x = response.readEntity(ObjectNode.class);
			throw new Neo4jException(x.path("message").asText());
			
		}
		System.out.println(response.getStatus());
		
		return (ObjectNode) response.readEntity(ObjectNode.class);
	}
}

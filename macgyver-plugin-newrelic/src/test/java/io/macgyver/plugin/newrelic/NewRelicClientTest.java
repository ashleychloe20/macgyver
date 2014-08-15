package io.macgyver.plugin.newrelic;

import java.io.IOException;

import io.macgyver.test.MacGyverIntegrationTest;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Form;

import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public class NewRelicClientTest extends MacGyverIntegrationTest {

	
	String apiKey;
	
	@org.junit.Before
	public void checkIfKeyIsAvailable() {
		apiKey = getPrivateProperty("newrelic.apiKey");	
	}
	
	@Test
	public void testX() throws IOException {
		Assume.assumeFalse(Strings.isNullOrEmpty(apiKey));
		NewRelicClient c = new NewRelicClient();
		c.setApiKey(apiKey);
		
		JsonNode x = c.get("applications");
		
		Assert.assertTrue(x.has("applications"));
		
		
	}
	
	
	@Test(expected=NotAuthorizedException.class)
	public void testInvalidKey() {

		NewRelicClient c = new NewRelicClient();
		c.setApiKey("invalid");
		
		JsonNode x = c.get("applications.json");
	
	}

}

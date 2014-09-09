package io.macgyver.plugin.splunk;

import java.io.IOException;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.plugin.splunk.SplunkClient;
import io.macgyver.test.MacGyverIntegrationTest;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Form;

import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public class SplunkServiceTest extends MacGyverIntegrationTest {

	@Autowired
	ServiceRegistry registry;
	
	
	String apiKey;
	
	@org.junit.Before
	public void checkIfKeyIsAvailable() {
	
	}
	
	@Test
	public void testX() throws IOException {
		
		
		
	}
	
	
	@Test(expected=NotAuthorizedException.class)
	public void testInvalidKey() {

		SplunkClient c = new SplunkClient();
		c.setApiKey("invalid");
		
		JsonNode x = c.get("applications.json");
	
	}

}

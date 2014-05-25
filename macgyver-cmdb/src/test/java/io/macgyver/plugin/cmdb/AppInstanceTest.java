package io.macgyver.plugin.cmdb;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppInstanceTest {

	@Test
	public void testVertexId() {
		assertEquals("eb40856932a1f1095a133af674a45ac46aec3b72", AppInstance.calculateVertexId("localhost", "myapp",""));
		assertEquals("eb40856932a1f1095a133af674a45ac46aec3b72", AppInstance.calculateVertexId("localhost", "myapp",null));
		assertEquals("eb40856932a1f1095a133af674a45ac46aec3b72", AppInstance.calculateVertexId("localhost", "myapp"));
	}
	
	@Test
	public void testProperty() {
		AppInstance ai = new AppInstance();
		assertEquals("123", ai.getStringProperty("foobar", "123"));
	}
	
	@Test
	public void testJackson() throws JsonProcessingException,IOException {
		AppInstance ai = new AppInstance();
		ai.setAppId("myapp");
		ai.setHost("myhost");
		ai.getProperties().put("foo.x", "bar");
		
		String json = new ObjectMapper().writeValueAsString(ai);
		
		
		AppInstance ai2 = new ObjectMapper().readValue(json, AppInstance.class);
		
		assertEquals(ai.getAppId(),ai2.getAppId());
		assertEquals(ai.getHost(),ai2.getHost());
		
		
	}
}

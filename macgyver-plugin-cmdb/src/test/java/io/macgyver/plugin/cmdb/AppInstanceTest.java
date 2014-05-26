package io.macgyver.plugin.cmdb;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppInstanceTest {

	@Test
	public void testVertexId() {
		assertEquals("0f1e5ec7c345a2701bbdf74f048ff49a54f32d85", AppInstance.calculateVertexId("localhost", "myapp",""));
		assertEquals("0f1e5ec7c345a2701bbdf74f048ff49a54f32d85", AppInstance.calculateVertexId("localhost", "myapp",null));
	}
	
	@Test
	public void testProperty() {
		AppInstance ai = new AppInstance("dummy","dummy");
		assertEquals("123", ai.getStringProperty("foobar", "123"));
	}
	
	@Test
	public void testJackson() throws JsonProcessingException,IOException {
		AppInstance ai = new AppInstance("myhost","myapp");
		
		ai.getProperties().put("foo.x", "bar");
		
		String json = new ObjectMapper().writeValueAsString(ai);
		
		
		AppInstance ai2 = new ObjectMapper().readValue(json, AppInstance.class);
		
		assertEquals(ai.getAppId(),ai2.getAppId());
		assertEquals(ai.getHost(),ai2.getHost());
		
		
	}
}

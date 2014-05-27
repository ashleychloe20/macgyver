package io.macgyver.plugin.cmdb;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppInstanceTest {

	@Test
	public void testVertexId() {
		assertEquals("726fb4a7375399fdeb4947760bf5e393bbf0e89c", AppInstance.calculateVertexId("localhost", "group","myapp",""));
		assertEquals("726fb4a7375399fdeb4947760bf5e393bbf0e89c", AppInstance.calculateVertexId("localhost", "group","myapp",null));
		AppInstance ai = new AppInstance("localhost","group","myapp","");
		
		assertEquals("726fb4a7375399fdeb4947760bf5e393bbf0e89c",ai.computeVertexId());
	}
	
	@Test
	public void testProperty() {
		AppInstance ai = new AppInstance("dummy","group","dummy");
		assertEquals("123", ai.getStringProperty("foobar", "123"));
	}
	
	@Test
	public void testJackson() throws JsonProcessingException,IOException {
		AppInstance ai = new AppInstance("myhost","group", "myapp");
		
		ai.getProperties().put("foo.x", "bar");
		
		String json = new ObjectMapper().writeValueAsString(ai);
		
		
		AppInstance ai2 = new ObjectMapper().readValue(json, AppInstance.class);
		
		assertEquals(ai.getAppId(),ai2.getAppId());
		assertEquals(ai.getHost(),ai2.getHost());
		
		
	}
}

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
		AppInstance ai = new AppInstance("dummy","group","app");
		assertEquals("dummy",ai.getHost());
		assertEquals("group",ai.getGroupId());
		assertEquals("app",ai.getArtifactId());
	}
	
	
}

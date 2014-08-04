package io.macgyver.plugin.cmdb;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppInstanceTest {


	
	@Test
	public void testProperty() {
		AppInstance ai = new AppInstance("dummy","group","app");
		assertEquals("dummy",ai.getHost());
		assertEquals("group",ai.getGroupId());
		assertEquals("app",ai.getAppId());
	}
	
	
}

package io.macgyver.plugin.elb.a10;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;

import io.macgyver.plugin.elb.a10.A10Client;
import io.macgyver.plugin.elb.a10.A10RemoteException;
import io.macgyver.test.MacGyverIntegrationTest;

public class A10ClientTest extends MacGyverIntegrationTest {

	@Test
	public void testRemoteException() throws IOException {
		String json = "{\n" + "  \"response\" : {\n"
				+ "    \"status\" : \"fail\",\n" + "    \"err\" : {\n"
				+ "      \"code\" : 1008,\n"
				+ "      \"msg\" : \"Invalid web service method name\"\n"
				+ "    }\n" + "  }\n" + "}";
		
		ObjectNode x = (ObjectNode) new ObjectMapper().readTree(json);
		
		try {
			A10Client client = new A10Client("http://localhost", "xx", "");
			client.throwExceptionIfNecessary(x);
			Assert.fail("exception not thrown");
		}
		catch (A10RemoteException e) {
			Assert.assertEquals("1008",e.getErrorCode());
			Assert.assertEquals("Invalid web service method name", e.getErrorMessage());
		}
	}
}
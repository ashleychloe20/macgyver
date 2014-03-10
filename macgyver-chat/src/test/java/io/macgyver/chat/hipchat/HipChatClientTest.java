package io.macgyver.chat.hipchat;

import io.macgyver.core.service.ServiceInstanceRegistry;
import io.macgyver.test.MacgyverIntegrationTest;

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ning.http.client.AsyncHttpClient;

public class HipChatClientTest extends MacgyverIntegrationTest {

	@Autowired
	@Qualifier("macgyverAsyncHttpClient")
	AsyncHttpClient client;

	@Autowired
	ServiceInstanceRegistry factory;
	
	
	@Test
	public void testIt() throws Exception {

		
		Assert.assertNotNull(factory.get("hipchatTest"));
		
		
		HipChat client = new HipChat(this.client);
		client.setApiKey("");
		client.sendMessage("test from "
				+ InetAddress.getLocalHost().getHostName(),
				"Notification Testing", "12345678901234", true, null);

		//Thread.sleep(5000);

	}
	
	
}

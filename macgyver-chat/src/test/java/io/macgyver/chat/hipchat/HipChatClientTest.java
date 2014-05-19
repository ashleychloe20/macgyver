package io.macgyver.chat.hipchat;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacIntegrationTest;

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ning.http.client.AsyncHttpClient;

public class HipChatClientTest extends MacIntegrationTest {

	@Autowired
	@Qualifier("macAsyncHttpClient")
	AsyncHttpClient client;

	@Autowired
	ServiceRegistry factory;
	
	
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

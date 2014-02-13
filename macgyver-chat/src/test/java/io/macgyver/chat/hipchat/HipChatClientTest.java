package io.macgyver.chat.hipchat;

import io.macgyver.test.MacgyverIntegrationTest;

import java.net.InetAddress;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ning.http.client.AsyncHttpClient;

public class HipChatClientTest extends MacgyverIntegrationTest {

	@Autowired
	@Qualifier("macgyverAsyncHttpClient")
	AsyncHttpClient client;

	@Test
	public void testIt() throws Exception {

		HipChat client = new HipChat(this.client);
		client.setApiKey("");
		client.sendMessage("test from "
				+ InetAddress.getLocalHost().getHostName(),
				"Notification Testing", "12345678901234", true, null);

		//Thread.sleep(5000);

	}
}

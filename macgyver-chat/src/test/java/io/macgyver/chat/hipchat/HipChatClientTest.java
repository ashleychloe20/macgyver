package io.macgyver.chat.hipchat;
import java.net.Inet4Address;
import java.net.InetAddress;

import io.macgyver.chat.hipchat.HipChatClient;

import org.junit.Test;


public class HipChatClientTest {

	
	@Test
	public void testIt() throws Exception {
		HipChatClient client = new HipChatClient();
		
		client.setApiKey("");
		client.sendMessage("test from "+InetAddress.getLocalHost().getHostName(), "Notification Testing", "12345678901234", true, null);
		
		Thread.sleep(5000);
	}
}

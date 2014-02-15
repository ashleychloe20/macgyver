package io.macgyver.chat.hipchat;

import io.macgyver.core.Kernel;
import io.macgyver.core.ServiceFactoryBean;
import io.macgyver.core.ServiceFactoryClassFinder;
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
	HipChat hipChat;
	
	@Test
	public void testIt() throws Exception {

		Assert.assertNotNull(hipChat);
		
		Assert.assertSame(hipChat,Kernel.getInstance().getApplicationContext().getBean("hipchatTest"));
		
		HipChat client = new HipChat(this.client);
		client.setApiKey("");
		client.sendMessage("test from "
				+ InetAddress.getLocalHost().getHostName(),
				"Notification Testing", "12345678901234", true, null);

		//Thread.sleep(5000);

	}
	
	@Test
	public void testServiceTypes() throws ClassNotFoundException {
		ServiceFactoryClassFinder locator = new ServiceFactoryClassFinder();
		Class<ServiceFactoryBean> clazz = locator.forServiceType("HiPcHaT");
		
		Assert.assertEquals(HipChatFactoryBean.class,clazz);
	}
}

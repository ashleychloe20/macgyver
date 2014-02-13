package io.macgyver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ning.http.client.AsyncHttpClient;

import io.macgyver.chat.hipchat.HipChat;
import io.macgyver.core.ServiceFactoryBean;

public class HipChatFactoryBean extends ServiceFactoryBean<HipChat> {

	@Autowired
	@Qualifier("macgyverAsyncHttpClient")
	AsyncHttpClient client;
	
	public HipChatFactoryBean() {
		super(HipChat.class);
	
	}

	@Override
	public HipChat createObject() throws Exception {
	
		HipChat c = new HipChat(client);
		c.setApiKey(getProperties().getProperty("apiKey"));
		return c;
	}

}

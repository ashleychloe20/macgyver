package io.macgyver.config;

import io.macgyver.chat.hipchat.HipChat;
import io.macgyver.core.ServiceFactoryBean;

public class HipChatFactoryBean extends ServiceFactoryBean<HipChat> {

	public HipChatFactoryBean() {
		super(HipChat.class);
	
	}

	@Override
	public HipChat createObject() throws Exception {
		HipChat c = new HipChat();
		c.setApiKey(getProperties().getProperty("apiKey"));
		return c;
	}

}

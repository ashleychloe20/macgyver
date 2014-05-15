package io.macgyver.plugin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.macgyver.chat.hipchat.HipChatServiceFactory;

@Configuration
public class HipChatConfig {

	@Bean
	public HipChatServiceFactory hipChatServiceFactory() {
		return new HipChatServiceFactory();
	}
	
}

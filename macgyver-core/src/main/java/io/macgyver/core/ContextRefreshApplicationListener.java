package io.macgyver.core;

import io.macgyver.core.eventbus.MacGyverEventBus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshApplicationListener implements
		ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	MacGyverEventBus eventBus;

	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		log.info("posting lifecycle event to EventBus: {}", event);

		eventBus.post(event);

		log.info("event post complete");
		
		
		eventBus.post(new Kernel.KernelStartedEvent());
	}

}

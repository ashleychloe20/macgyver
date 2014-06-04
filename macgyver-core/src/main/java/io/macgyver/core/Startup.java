package io.macgyver.core;

import io.macgyver.core.auth.InternalAuthenticationProvider;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.core.script.ScriptExecutor;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class Startup implements InitializingBean {

	Logger logger = LoggerFactory.getLogger(Startup.class);

	@Autowired
	EventBus bus;

	@Autowired
	Kernel kernel;


	@Autowired
	InternalAuthenticationProvider internalAuthenticationProvider;

	@Autowired
	ExtensionResourceProvider resourceLoader;
	
	@Subscribe
	public void onStart(ContextRefreshedEvent event) throws IOException {
		if (kernel.getApplicationContext() != event.getSource()) {
			return;
		}
		logger.info("STARTED: {}", event);
		runInitScripts();
		seedMapDB();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		bus.register(this);


	}

	public void runInitScripts() throws IOException {
		
		for (Resource r: resourceLoader.findFileResources()) {
			if (r.getPath().startsWith("scripts/init/")) {
				runInitScript(r);
			}
		
		}
		

	}

	public void runInitScript(Resource resource) throws IOException {
		
		
	
		ScriptExecutor se = new ScriptExecutor();
		if (se.isSupportedScript(resource)) {
			try {
				se.run(resource, null, false);
			}
			catch (RuntimeException e) {
				kernel.registerStartupError(e);
				throw e;
			}
		} else {
			logger.info("ignoring file in init script dir: {}", resource);
		}
	}

	protected void seedMapDB() {

		internalAuthenticationProvider.seedData();

	}
}

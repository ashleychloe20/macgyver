package io.macgyver.core;

import io.macgyver.core.auth.InternalAuthenticationProvider;
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
	VfsManager vfsManager;
	
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
		File initScriptsFileObject = vfsManager.resolveScript("init");
		
		
		if (!initScriptsFileObject.exists() ) {
			logger.info("init scripts dir does not exist: {}", initScriptsFileObject);
			return;
		}
		
		
		
		for (File child: initScriptsFileObject.listFiles()) {
			runInitScript(child);
		}
	
	}

	public void runInitScript(File f) throws IOException {
		
		
		if (f.isDirectory()) {
			return;
		}
		ScriptExecutor se = new ScriptExecutor();
		if (se.isSupportedScript(f)) {
			try {
				// TODO FIXME
		//		se.run(f, null, false);
			}
			catch (RuntimeException e) {
				kernel.registerStartupError(e);
				throw e;
			}
		} else {
			logger.info("ignoring file in init script dir: {}", f);
		}
	}

	protected void seedMapDB() {

		internalAuthenticationProvider.seedData();

	}
}

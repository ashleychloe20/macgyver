package io.macgyver.core;

import io.macgyver.core.auth.InternalAuthenticationProvider;
import io.macgyver.core.script.ScriptExecutor;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.mapdb.TxMaker;
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
	TxMaker txMaker;

	@Autowired
	InternalAuthenticationProvider internalAuthenticationProvider;

	@Autowired
	VirtualFileSystem vfsManager;
	
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
		FileObject initScriptsFileObject = vfsManager.getScriptsLocation().resolveFile("init");
		
		
		if (!initScriptsFileObject.exists() ) {
			logger.info("init scripts dir does not exist: {}", initScriptsFileObject);
			return;
		}
		
		FileObject [] childObjects = initScriptsFileObject.getChildren();
		
		for (int i=0; childObjects!=null && i<childObjects.length; i++) {
			runInitScript(childObjects[i]);
		}
	
	}

	public void runInitScript(FileObject f) throws IOException {
		
		
		if (f.getType().equals(FileType.FOLDER)) {
			return;
		}
		ScriptExecutor se = new ScriptExecutor();
		if (se.isSupportedScript(f)) {
			try {
				se.run(f, null, false);
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

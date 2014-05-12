package io.macgyver.core;

import io.macgyver.core.script.ScriptExecutor;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.TxBlock;
import org.mapdb.TxMaker;
import org.mapdb.TxRollbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.common.collect.TreeTraverser;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;

public class Startup implements InitializingBean {

	Logger logger = LoggerFactory.getLogger(Startup.class);

	@Autowired
	EventBus bus;

	@Autowired
	Kernel kernel;

	@Autowired
	TxMaker txMaker;
	
	@Subscribe
	public void onStart(ContextRefreshedEvent event) {
		if (kernel.getApplicationContext()!=event.getSource()) {
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

	public void runInitScripts() {

		File initRoot = new File(kernel.getExtensionDir(), "scripts/init");
		if (!initRoot.exists() || !initRoot.isDirectory()) {
			logger.info("init scripts dir does not exist: {}", initRoot);
			return;
		}
		TreeTraverser<File> traverser = Files.fileTreeTraverser();

		FluentIterable<File> t = traverser.preOrderTraversal(initRoot);
		Iterator<File> x = t.iterator();
		while (x.hasNext()) {
			File f = x.next();
			runInitScript(f);
		}

	}



	public void runInitScript(File f) {
		if (f.isDirectory()) {
			return;
		}
		ScriptExecutor se = new ScriptExecutor();
		if (se.isSupportedScript(f)) {
			try {
				se.run(f, null, false);
			} catch (RuntimeException e) {
				Kernel.registerStartupError(e);
			}
		} else {
			logger.info("ignoring file in init script dir: {}", f);
		}
	}
	
	protected void seedMapDB() {
		TxBlock b = new TxBlock() {
			
			@Override
			public void tx(DB db) throws TxRollbackException {
				Map<String,Map<String,String>> m = db.getHashMap("internal_passwd");
				
				if (!m.containsKey("admin")) {
					System.out.println("Addming admin");
					Map<String,String> adminEntry = Maps.newHashMap();
					adminEntry.put("username", "admin");
					m.put("admin", adminEntry);
				}
				
			}
		};
		txMaker.execute(b);
	}
}

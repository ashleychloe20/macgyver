package io.macgyver.core.scheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import io.macgyver.core.Kernel;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static java.nio.file.StandardWatchEventKinds.*;

public class AutoSchedulerFileWatcher implements Runnable {

	Logger logger = LoggerFactory.getLogger(AutoSchedulerFileWatcher.class);

	@Autowired
	AutoScheduler autoScheduler;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	Kernel kernel;

	WatchService watcher;

	@PostConstruct
	public void init() {
		
		try {
		watcher = FileSystems.getDefault().newWatchService();
		Path p = new File(Kernel.getInstance().getExtensionDir(),"scripts").toPath();
		
			WatchKey key = p.register(watcher, ENTRY_CREATE, ENTRY_DELETE,
					ENTRY_MODIFY);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Thread t = new Thread(this, "AutoSchedulerWatcher");
		t.start();
	}

	@Override
	public void run() {
		WatchKey key=null;
		for (;;) {
			try {
				
			        key = watcher.take();
			        handleWatchKey(key);
			    
			} catch (Exception e) {
				logger.warn("", e);
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException IGNORE) {
				}
			}
			finally {
				if (key!=null) {
					  boolean valid = key.reset();
					    
				}
			}
		}

	}
	
	protected void handleWatchKey(WatchKey key) {
		 for (WatchEvent<?> event: key.pollEvents()) {
		        WatchEvent.Kind<?> kind = event.kind();

		        // This key is registered only
		        // for ENTRY_CREATE events,
		        // but an OVERFLOW event can
		        // occur regardless if events
		        // are lost or discarded.
		        if (kind == OVERFLOW) {
		            continue;
		        }

		        // The filename is the
		        // context of the event.
		        WatchEvent<Path> ev = (WatchEvent<Path>)event;
		        Path filename = ev.context();
		        logger.info("FILE: "+filename);
		       

		        // Email the file to the
		        //  specified email alias.
		       
		    }
	}
}

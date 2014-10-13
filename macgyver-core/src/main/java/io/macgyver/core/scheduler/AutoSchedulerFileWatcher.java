/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.core.scheduler;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import io.macgyver.core.Kernel;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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

	
	}

	@Override
	public void run() {
		WatchKey key = null;
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
			} finally {
				if (key != null) {
					boolean valid = key.reset();

				}
			}
		}

	}

	protected void handleWatchKey(WatchKey key) {
		for (WatchEvent<?> event : key.pollEvents()) {
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
			WatchEvent<Path> ev = (WatchEvent<Path>) event;
			Path filename = ev.context();
			logger.info("FILE: " + filename);

			// Email the file to the
			// specified email alias.

		}
	}
}

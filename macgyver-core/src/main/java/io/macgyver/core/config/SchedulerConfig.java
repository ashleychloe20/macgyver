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
package io.macgyver.core.config;

import io.macgyver.core.scheduler.MacGyverScheduleListener;
import io.macgyver.core.scheduler.ScheduleScanner;
import io.macgyver.core.scheduler.MacGyverTaskCollector;
import io.macgyver.neorx.rest.NeoRxClient;
import it.sauronsoftware.cron4j.SchedulerListener;
import it.sauronsoftware.cron4j.TaskCollector;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {

	@Value("${scheduler.enabled:true}")
	boolean schedulerEnabled = true;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	NeoRxClient neo4j;
	
	

	
	@Bean
	public ScheduleScanner macAutoScheduler() {
		return new ScheduleScanner();
	}


	@Bean
	public TaskCollector macTaskCollector() {
		return new MacGyverTaskCollector();
	}
	
	@Bean
	public SchedulerListener macSchedulerListener() {
		return new MacGyverScheduleListener();
	}
	@Bean
	public it.sauronsoftware.cron4j.Scheduler macScheduler() {
		final it.sauronsoftware.cron4j.Scheduler scheduler = new it.sauronsoftware.cron4j.Scheduler();
		scheduler.addTaskCollector(macTaskCollector());
		scheduler.setDaemon(true);
		scheduler.addSchedulerListener(macSchedulerListener());
		LoggerFactory.getLogger(SchedulerConfig.class).info("starting scheduler: {}",scheduler);
		scheduler.start();
		
		Runnable r = new Runnable() {

			@Override
			public void run() {
				LoggerFactory.getLogger(it.sauronsoftware.cron4j.Scheduler.class).info("heartbeat");
				
			}
			
		};
		String key = scheduler.schedule("* * * * *",r);
		
		
		return scheduler;
	}
	
}

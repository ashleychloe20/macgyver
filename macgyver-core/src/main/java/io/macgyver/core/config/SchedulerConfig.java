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

import io.macgyver.core.scheduler.AutoScheduler;
import io.macgyver.core.scheduler.AutoWiringSpringJobFactory;
import io.macgyver.core.scheduler.SchedulerUtil;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

	@Value("${scheduler.enabled:true}")
	boolean schedulerEnabled = true;

	@Autowired
	ApplicationContext applicationContext;

	@Bean
	public SchedulerFactoryBean macSchedulerFactoryBean() {

		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setAutoStartup(schedulerEnabled);

		// This prevents Quartz from checking for updates
		// http://quartz-scheduler.org/documentation/best-practices
		Properties quartzProperties = new Properties();
		quartzProperties.setProperty("org.quartz.scheduler.skipUpdateCheck",
				"true");
		schedulerFactoryBean.setQuartzProperties(quartzProperties);

		AutoWiringSpringJobFactory jobFactory = new AutoWiringSpringJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		schedulerFactoryBean.setJobFactory(jobFactory);
		return schedulerFactoryBean;
	}

	@Bean
	public AutoWiringSpringJobFactory macSpringJobFactory() {
		return new AutoWiringSpringJobFactory();
	}

	@Bean
	public SchedulerUtil macSchedulerUtil() {
		return new SchedulerUtil();
	}
	

	
	@Bean
	public AutoScheduler macAutoScheduler() {
		return new AutoScheduler();
	}

	
}

package io.macgyver.config;

import io.macgyver.core.script.BindingSupplier;
import io.macgyver.scheduler.AutoScheduler;
import io.macgyver.scheduler.AutoSchedulerFileWatcher;
import io.macgyver.scheduler.AutoWiringSpringJobFactory;
import io.macgyver.scheduler.SchedulerUtil;

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
	public SchedulerFactoryBean scheduler() {

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
	public AutoWiringSpringJobFactory springJobFactory() {
		return new AutoWiringSpringJobFactory();
	}

	@Bean
	public SchedulerUtil schedulerUtil() {
		return new SchedulerUtil();
	}
	

	
	@Bean
	public AutoScheduler autoScheduler() {
		return new AutoScheduler();
	}

	
}

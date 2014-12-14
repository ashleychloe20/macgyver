package io.macgyver.core.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.sauronsoftware.cron4j.SchedulerListener;
import it.sauronsoftware.cron4j.TaskExecutor;

public class MacGyverScheduleListener implements SchedulerListener {

	Logger logger = LoggerFactory.getLogger(MacGyverScheduleListener.class);
	
	@Override
	public void taskFailed(TaskExecutor taskExecutor, Throwable exception) {
		logger.warn("taskFailed - "+taskExecutor.getTask(),exception);

	}

	@Override
	public void taskLaunching(TaskExecutor taskExecutor) {
		logger.info("taskLaunching: {}",taskExecutor.getTask());

	}

	@Override
	public void taskSucceeded(TaskExecutor taskExecutor) {
		logger.info("taskSucceeded: {}",taskExecutor.getTask());

	}

}

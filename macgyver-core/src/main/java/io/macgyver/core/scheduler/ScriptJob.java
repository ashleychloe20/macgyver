package io.macgyver.core.scheduler;

import io.macgyver.core.Kernel;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.script.ScriptExecutor;

import java.io.File;
import java.io.IOException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class ScriptJob implements Job {

	Logger logger = LoggerFactory.getLogger(getClass());
	public static final String SCRIPT_PATH_KEY = "scriptPath";

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String key = context.getJobDetail().getJobDataMap()
					.getString(ScriptJob.SCRIPT_PATH_KEY);
			AutoScheduler autoScheduler = Kernel.getInstance()
					.getApplicationContext().getBean(AutoScheduler.class);

			Resource r = autoScheduler.scriptResourceMap.get(key);
			if (r != null) {
				logger.info("executing: " + r);

				ScriptExecutor executor = new ScriptExecutor();

				executor.run(r, null, true);
			} else {
				logger.warn("resource not found: {}", r);
			}
		} catch (RuntimeException e) {
			throw new JobExecutionException(e);
		}
	}

}

package io.macgyver.scheduler;

import java.io.File;

import io.macgyver.core.script.ScriptExecutor;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptJob implements Job {

	Logger logger = LoggerFactory.getLogger(getClass());
	public static final String SCRIPT_PATH_KEY = "scriptPath";

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String path = context.getJobDetail().getJobDataMap()
				.getString(ScriptJob.SCRIPT_PATH_KEY);
		File f = new File(path);
		logger.info("executing: " + path);

		ScriptExecutor executor = new ScriptExecutor();
		executor.run(f,null,true);
	}

}

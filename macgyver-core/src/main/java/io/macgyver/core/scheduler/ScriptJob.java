package io.macgyver.core.scheduler;

import io.macgyver.core.script.ScriptExecutor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
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
			String path = context.getJobDetail().getJobDataMap()
					.getString(ScriptJob.SCRIPT_PATH_KEY);
			File f = new File(path);

			FileObject target = VFS.getManager().resolveFile(
					f.getAbsoluteFile().toURI().toURL().toString());
			logger.info("executing: " + target);

			ScriptExecutor executor = new ScriptExecutor();
			executor.run(target, null, true);
		} catch (IOException  e) {
			throw new JobExecutionException(e);
		}
	}

}

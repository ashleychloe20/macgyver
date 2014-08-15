package io.macgyver.core.scheduler;

import io.macgyver.core.Kernel;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.core.script.ScriptExecutor;

import java.io.File;
import java.io.IOException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

@DisallowConcurrentExecution
public class ScriptJob implements Job {

	Logger logger = LoggerFactory.getLogger(getClass());
	public static final String SCRIPT_PATH_KEY = "scriptPath";

	public static final String SCRIPT_HASH_KEY = "scriptHash";

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			Optional<Resource> r = Optional.absent();
			String hashKey = context.getJobDetail().getJobDataMap()
					.getString(ScriptJob.SCRIPT_HASH_KEY);
			if (!Strings.isNullOrEmpty(hashKey)) {
				ExtensionResourceProvider rp = Kernel.getInstance()
				.getApplicationContext().getBean(ExtensionResourceProvider.class);
				r = rp.findResourceByHash(hashKey);
			} else {
				String key = context.getJobDetail().getJobDataMap()
						.getString(ScriptJob.SCRIPT_PATH_KEY);
				AutoScheduler autoScheduler = Kernel.getInstance()
						.getApplicationContext().getBean(AutoScheduler.class);

				r = Optional.fromNullable(autoScheduler.scriptResourceMap.get(key));
			}
			
		
			if (r.isPresent()) {
				logger.debug("executing: {}", r);

				ScriptExecutor executor = new ScriptExecutor();

				executor.run(r.get(), null, true);
			} else {
				logger.warn("resource not found: {}", r);
			}
		} catch (IOException | RuntimeException e) {
			throw new JobExecutionException(e);
		}
		
	}

}

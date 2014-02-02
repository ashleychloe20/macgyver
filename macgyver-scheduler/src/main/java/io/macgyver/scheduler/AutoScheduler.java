package io.macgyver.scheduler;

import io.macgyver.core.Kernel;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.LineProcessor;

public class AutoScheduler implements InitializingBean {

	Logger logger = LoggerFactory.getLogger(AutoScheduler.class);
	@Autowired
	Scheduler scheduler;

	@Autowired
	Kernel kernel;

	public static String AUTO_SCHEDULER_GROUP = "AUTO_SCHEDULER";

	public static class CrontabLineProcessor implements
			LineProcessor<Optional<JsonObject>> {
		int i = 0;
		String result;

		@Override
		public boolean processLine(String line) throws IOException {
			if (i++ > 50) {
				// only look through the first 50 lines
				return false;
			}
			if (line != null && line.contains(SCHEDULE_TOKEN)) {
				result = line.substring(
						line.indexOf(SCHEDULE_TOKEN) + SCHEDULE_TOKEN.length())
						.trim();
				return false;
			}
			return true;
		}

		@Override
		public Optional<JsonObject> getResult() {
			if (result == null || result.trim().length() == 0) {
				return Optional.absent();
			}

			JsonObject obj = Json.createReader(new StringReader(result))
					.readObject();
			return Optional.fromNullable(obj);
		}

	}

	public static class ScanJob implements Job {

		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			try {
				Kernel.getInstance().getApplicationContext()
						.getBean(AutoScheduler.class).scan();
			} catch (IOException e) {
				throw new JobExecutionException(e);
			}

		}
	}

	public void scan() throws IOException {
		Set<JobKey> validJobKeySet = Sets.newConcurrentHashSet();

		String scanId = UUID.randomUUID().toString();

		final List<ScheduledScript> list = Lists.newArrayList();
		final FileVisitor fv = new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(final Path dir,
					BasicFileAttributes attrs) throws IOException {
				// TODO Auto-generated method stub
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				// TODO Auto-generated method stub
				Optional<JsonObject> schedule = extractCronExpression(file
						.toFile());
				if (schedule.isPresent()) {
					JsonObject descriptor = schedule.get();

					if (descriptor.containsKey("cron")) {
						String cronExpression = schedule.get()
								.getString("cron");

						ScheduledScript ss = new ScheduledScript(file.toFile(),
								cronExpression);

						list.add(ss);
					}

				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}
		};

		Files.walkFileTree(new File(Kernel.getInstance().getExtensionDir(),
				"scripts/scheduler").toPath(), fv);

		for (ScheduledScript ss : list) {
			try {
				scheduleScript(ss, validJobKeySet);
			} catch (Exception e) {
				logger.error("problem scheduling script: {}", scheduler);
			}
		}
		prune(validJobKeySet);

	}

	public void scheduleScript(ScheduledScript s, Set<JobKey> validSet)
			throws SchedulerException, IOException, ParseException {

		JobDetail jd = scheduler.getJobDetail(s.getJobKey());
		if (jd != null) {

			validSet.add(s.getJobKey());

			List<Trigger> triggers = (List<Trigger>) scheduler
					.getTriggersOfJob(s.getJobKey());

			for (Trigger trigger : triggers) {
				if (trigger instanceof CronTrigger) {
					CronTrigger ct = (CronTrigger) trigger;
					String existingCronExpression = ct.getCronExpression();
					String newCronExpression = s.cronExpression;
					if (!existingCronExpression.equals(newCronExpression)) {
						logger.info("unregistering the job: {}", s.getJobKey());
						scheduler.deleteJob(s.getJobKey());
						jd = null;
					} else {
						// already registered
					}
				}
			}
			return;
		}
		if (jd == null) {
			validSet.add(s.getJobKey());
			jd = JobBuilder
					.newJob()
					.ofType(ScriptJob.class)
					.withIdentity(s.getJobKey())
					.usingJobData(ScriptJob.SCRIPT_PATH_KEY,
							s.scriptFile.getCanonicalPath()).build();
			CronTriggerImpl cti = new CronTriggerImpl();
			cti.setCronExpression(s.cronExpression);
			cti.setName(UUID.randomUUID().toString());
			scheduler.scheduleJob(jd, cti);
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {

		JobDetail jd = JobBuilder.newJob(ScanJob.class)
				.usingJobData("_code", "test").withDescription("test")
				.withIdentity("AutoScheduler.scan").build();

		CronTriggerImpl cti = new CronTriggerImpl();

		cti.setCronExpression("*/5 * * * * ?");
		cti.setName(UUID.randomUUID().toString());

		scheduler.scheduleJob(jd, cti);

	}

	public static final String SCHEDULE_TOKEN = "#@Schedule";

	public static Optional<JsonObject> extractCronExpression(File f)
			throws IOException {

		return com.google.common.io.Files.readLines(f, Charsets.UTF_8,
				new CrontabLineProcessor());

	}

	protected void prune(Set<JobKey> validKeys) {
		try {
			Set<JobKey> keys = scheduler.getJobKeys(GroupMatcher
					.jobGroupEquals(AUTO_SCHEDULER_GROUP));

			for (JobKey key : keys) {
				if (!validKeys.contains(key)) {
					logger.info("deleting job: {}", key);
					scheduler.deleteJob(key);
				}
			}
		} catch (SchedulerException e) {
			logger.warn("", e);
		}
	}

}

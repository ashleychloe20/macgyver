package io.macgyver.core.scheduler;

import io.macgyver.core.Kernel;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.core.script.ScriptExecutor;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;

public class AutoScheduler implements InitializingBean {

	static Logger logger = LoggerFactory.getLogger(AutoScheduler.class);
	@Autowired
	Scheduler scheduler;

	@Autowired
	Kernel kernel;

	@Autowired
	ExtensionResourceProvider extensionLoader;

	Map<String, Resource> scriptResourceMap = Maps.newConcurrentMap();

	public static String AUTO_SCHEDULER_GROUP = "AUTO_SCHEDULER";

	public static class CrontabLineProcessor implements
			LineProcessor<Optional<ObjectNode>> {
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
		public Optional<ObjectNode> getResult() {
			if (result == null || result.trim().length() == 0) {
				return Optional.absent();
			}
			try {
				ObjectNode n = (ObjectNode) new ObjectMapper().readTree(result);

				return Optional.fromNullable(n);
			} catch (IOException e) {
				logger.warn("problem parsing: {}", result);
				return Optional.absent();
			}
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
		ScriptExecutor se = new ScriptExecutor();
		for (Resource r : extensionLoader.findResources()) {

			if (r.getPath().startsWith("scripts/scheduler/")) {
				logger.debug("scanning {} {}", r, r.getHash());
				Optional<ObjectNode> schedule = extractCronExpression(r);
				if (schedule.isPresent()) {
					String enabledStringVal = schedule.get().path("enabled")
							.asText();
					if (Strings.isNullOrEmpty(enabledStringVal)) {
						enabledStringVal = "true"; // default to true
					}
					boolean b = Boolean.parseBoolean(enabledStringVal);

					if (b) {
						if (se.isSupportedScript(r)) {
							ObjectNode descriptor = schedule.get();
							if (descriptor.has("cron")) {
								String cronExpression = schedule.get()
										.path("cron").asText();
								ScheduledScript ss = new ScheduledScript(r,
										cronExpression);
								list.add(ss);
							}
						} else {
							logger.warn("script type not supported: {}", r);
						}
					} else {
						logger.debug("script is disabled: {}", r);
					}
				} else {
					logger.debug("script does not have scheduler directive: {}",
							r);
				}
			}
		}

		for (ScheduledScript ss : list) {
			try {

				scheduleScript(ss, validJobKeySet);
			} catch (Exception e) {
				logger.error("problem scheduling {} - {}", ss, e.toString());
			}
		}

		prune(validJobKeySet);

	}

	public JobKey scheduleImmediate(Resource r) throws SchedulerException {
		try {
			JobKey jobKey = JobKey.jobKey("immediate-"
					+ UUID.randomUUID().toString(), "immediate-queue");
			JobDetail job = JobBuilder.newJob(ScriptJob.class)
					.withIdentity(jobKey)
					.usingJobData(ScriptJob.SCRIPT_HASH_KEY, r.getHash())
					.build();

			Trigger trigger = TriggerBuilder.newTrigger().forJob(job)
					.startNow().build();
			scheduler.scheduleJob(job,trigger);
			
		
			
			return jobKey;
			
		} catch (Exception e) {
			throw new SchedulerException(e);
		}

		// scheduler.addJob(jobDetail, replace);
	}

	@SuppressWarnings("unchecked")
	public void scheduleScript(ScheduledScript s, Set<JobKey> validSet)
			throws SchedulerException, IOException, ParseException {
		scriptResourceMap.put(s.getJobKey().toString(), s.resource);
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
						logger.debug("job already registered: {}", s);
					}
				}
			}
			return;
		} else {
			validSet.add(s.getJobKey());
			jd = JobBuilder
					.newJob()
					.ofType(ScriptJob.class)
					.withIdentity(s.getJobKey())
					.usingJobData(ScriptJob.SCRIPT_PATH_KEY,
							s.getJobKey().toString()).build();

			CronTriggerImpl cti = new CronTriggerImpl();
			cti.setCronExpression(s.cronExpression);
			cti.setName(s.resource.getHash());
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

	public static Optional<ObjectNode> extractCronExpression(Resource r) {

		try (StringReader sr = new StringReader(r.getContentAsString())) {
			return CharStreams.readLines(sr, new CrontabLineProcessor());
		} catch (IOException | RuntimeException e) {
			try {
				logger.warn("unable to extract cron expression: ",
						r.getContentAsString());
			} catch (Exception IGNORE) {
				logger.warn("unable to extract cron expression");
			}
		}

		return Optional.absent();

	}

	protected void prune(Set<JobKey> validKeys) {
		try {
			Set<JobKey> keys = scheduler.getJobKeys(GroupMatcher
					.jobGroupEquals(AUTO_SCHEDULER_GROUP));

			for (JobKey key : keys) {
				if (!validKeys.contains(key)) {
					logger.info("deleting job: {}", key);
					scheduler.deleteJob(key);
					scriptResourceMap.remove(key.toString());
				}
			}
		} catch (SchedulerException e) {
			logger.warn("", e);
		}

	}

}

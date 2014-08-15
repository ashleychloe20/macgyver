package io.macgyver.core.scheduler;

import io.macgyver.core.resource.Resource;

import java.io.File;
import java.io.IOException;

import org.quartz.JobKey;

import com.google.common.base.Objects;

public class ScheduledScript {
	Resource resource;
	String cronExpression;

	public ScheduledScript(Resource r, String cron) {
		this.resource = r;
		this.cronExpression = cron;
	}

	public String toString() {
		return Objects.toStringHelper(this)
				.add("resource", resource)
				.add("cronExpression", cronExpression).toString();
	}

	JobKey getJobKey() {
		try {
			if (resource==null) {
				return null;
			}
			return JobKey.jobKey(resource.getHash(),
					AutoScheduler.AUTO_SCHEDULER_GROUP);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

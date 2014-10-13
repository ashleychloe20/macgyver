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

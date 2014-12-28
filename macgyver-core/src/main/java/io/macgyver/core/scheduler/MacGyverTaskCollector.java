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

import io.macgyver.neorx.rest.NeoRxClient;
import it.sauronsoftware.cron4j.SchedulingPattern;
import it.sauronsoftware.cron4j.TaskCollector;
import it.sauronsoftware.cron4j.TaskTable;

import java.util.List;

import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

public class MacGyverTaskCollector implements TaskCollector {

	Logger logger = LoggerFactory.getLogger(MacGyverTaskCollector.class);

	@Autowired
	NeoRxClient client;

	public MacGyverTaskCollector() {

	}

	public MacGyverTaskCollector(NeoRxClient c) {
		this.client = c;
	}

	public List<JsonNode> fetchSchedule() {
		List<JsonNode> list = client
				.execCypherAsList("match (s:ScheduledTask) return s");

		return list;
	}

	String enhanceCronExpression(String input) {
		input = input.trim();
		String output = input;
		if (output.contains("?")) {
			logger.warn("TRANSITIONAL: cron expression looks like a quartz expression: "
					+ input);
			output = output.replace("?", "*");

		}
		String[] tokens = Strings.split(output, ' ');
		if (tokens.length > 5) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < tokens.length; i++) {
				if (i > 0) {
					sb.append(tokens[i] + " ");
				}
			}
			output = sb.toString().trim();

		}
		if (!input.equals(output)) {
			logger.warn("TRANSITIONAL: converted cron expression from '{}' to '{}'", input,
					output);
		}
		return output;
	}

	protected TaskTable toTaskTable(List<JsonNode> list) {
		TaskTable tt = new TaskTable();
		for (JsonNode n : list) {
			try {

				String cron = n.path("cron").asText();
				boolean enabled = n.path("enabled").asBoolean(true);

				if (enabled
						&& !com.google.common.base.Strings.isNullOrEmpty(cron)) {
					MacGyverTask t = new MacGyverTask(n);
					tt.add(new SchedulingPattern(enhanceCronExpression(cron)),
							t);
				}
			} catch (RuntimeException e) {
				logger.warn("coulld not schedule task: " + n, e);
			}

		}
		return tt;
	}

	@Override
	public TaskTable getTasks() {

		return toTaskTable(fetchSchedule());
	}

}

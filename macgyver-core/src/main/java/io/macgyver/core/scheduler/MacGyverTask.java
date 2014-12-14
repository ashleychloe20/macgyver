package io.macgyver.core.scheduler;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.script.ScriptExecutor;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;

public class MacGyverTask extends Task {

	Logger logger = LoggerFactory.getLogger(MacGyverTask.class);
	JsonNode config;

	public MacGyverTask(JsonNode n) {
		Preconditions.checkNotNull(n);
		this.config = n;
	}

	@Override
	public void execute(TaskExecutionContext context) throws RuntimeException {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("execute {} context={}", this, context);
			}
			ScriptExecutor se = new ScriptExecutor();

			Map<String, Object> args = createArgsFromConfig();

			se.run(config.path("script").asText(), args, true);
		} catch (IOException e) {
			throw new MacGyverException(e);
		}
	}

	public String toString() {
		return "MacGyverTask" + config.toString();
	}

	Map<String, Object> createArgsFromConfig() {
		Map<String, Object> args = com.google.common.collect.Maps.newHashMap();

		Iterator<String> t = config.fieldNames();
		while (t.hasNext()) {
			String param = t.next();

			JsonNode val = config.get(param);
			if (val.isValueNode()) {
				args.put(param, val.asText());
			}
		}

		return args;
	}
}

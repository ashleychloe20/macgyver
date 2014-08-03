package io.macgyver.core;

import io.macgyver.core.script.ScriptExecutor;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;

public class ScriptHookManager {

	Logger logger = LoggerFactory.getLogger(ScriptHookManager.class);
	@Autowired
	Kernel kernel;



	public Object invokeHook(String name, Map<String, Object> data)
			throws IOException {

		Preconditions.checkNotNull(name);

		String vname = "hooks/" + name + ".groovy";

		logger.info("running hook script: {}", vname);
		logger.info("hook script vars: {}", data);

		ScriptExecutor se = new ScriptExecutor();

		return se.run(vname, data, false);

	}
}

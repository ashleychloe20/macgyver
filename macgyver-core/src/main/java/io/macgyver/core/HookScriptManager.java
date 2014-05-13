package io.macgyver.core;

import io.macgyver.core.script.ScriptExecutor;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;

public class HookScriptManager {

	Logger logger = LoggerFactory.getLogger(HookScriptManager.class);
	@Autowired
	Kernel kernel;
	
	public void invokeHook(String name, Map<String,Object> data) {
		Preconditions.checkNotNull(name);
		
		File hooksDir = kernel.getExtensionDir("scripts/hooks");
		
		File hookFile = new File(hooksDir,name+".groovy");
		
		logger.info("running hook script: {}",hookFile);
		logger.info("hook script vars: {}",data);
		
		if (hookFile.exists()) {
			ScriptExecutor se = new ScriptExecutor();
			se.run(hookFile, data, false);
		}
		else {
			logger.info("hook file not found: {}",hookFile);
		}
	}
}

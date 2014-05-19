package io.macgyver.core;

import io.macgyver.core.script.ScriptExecutor;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;

public class ScriptHookManager {

	Logger logger = LoggerFactory.getLogger(ScriptHookManager.class);
	@Autowired
	Kernel kernel;

	@Autowired
	VirtualFileSystem vfsManager;
	
	public Object invokeHook(String name, Map<String, Object> data) throws IOException {
		Preconditions.checkNotNull(name);

		FileObject hookFile = vfsManager.getScriptsLocation().resolveFile("hooks/"+name+".groovy");
		
		

	
		logger.info("running hook script: {}", hookFile);
		logger.info("hook script vars: {}", data);

		if (hookFile.exists()) {
			ScriptExecutor se = new ScriptExecutor();
			return se.run(hookFile, data, false);
		} else {
			logger.info("hook file not found: {}", hookFile);
		}
		return null;
	}
}

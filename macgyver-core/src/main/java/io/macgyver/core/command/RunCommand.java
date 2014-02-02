package io.macgyver.core.command;

import io.macgyver.core.Kernel;
import io.macgyver.core.script.ScriptExecutor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class RunCommand extends Command {

	Logger logger = LoggerFactory.getLogger(RunCommand.class);

	@Override
	public void doInvoke(CommandLine commandLine) {

			String script = commandLine.getArgList().get(1).toString();

			ScriptExecutor se = new ScriptExecutor();
			se.run(script);
		
	}

	public Options getOptions() {
		Options opts = new Options();

		return opts;
	}
}

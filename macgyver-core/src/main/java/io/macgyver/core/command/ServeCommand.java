package io.macgyver.core.command;

import io.macgyver.core.Kernel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServeCommand extends Command {
	
	Logger logger = LoggerFactory.getLogger(ServeCommand.class);
	
	@Override
	public void doInvoke(CommandLine commandLine) {
		
		
		while (!terminated()) {
			try {
			
				Thread.sleep(1000);
			}
			catch (Exception e) {
				
			}
		}
		logger.info("terminating...");
		
	}

	@Override
	public Options getOptions() {
		return new Options();
	}

	boolean terminated() {
		return !Kernel.getInstance().isRunning();
	}
}

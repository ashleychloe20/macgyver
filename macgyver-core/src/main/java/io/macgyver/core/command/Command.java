package io.macgyver.core.command;

import io.macgyver.core.Kernel;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class Command {

	org.slf4j.Logger logger = LoggerFactory.getLogger(Command.class);
	
	CommandLine commandLine;

	protected void doInitializeKernel() {
		
		Kernel.initialize();

	}

	protected void doShutdownKernel() {
		Kernel k = null;
		
		try {
		k = Kernel.getInstance();
		}
		catch (IllegalStateException e) {
			return;
		}
		ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) k
				.getInstance().getApplicationContext();
		ctx.close();
	}

	public void execute(String[] args) throws ParseException {
		BasicParser bp = new BasicParser();
		this.commandLine = bp.parse(getOptions(), args);
		try {
			
			doInitializeKernel();
			
			doInvoke(this.commandLine);
			
		}
		finally {
			doShutdownKernel();
		}
	}

	public abstract void doInvoke(CommandLine commandLine);

	public abstract Options getOptions();

}

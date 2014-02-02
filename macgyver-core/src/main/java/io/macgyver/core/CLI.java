package io.macgyver.core;

import io.macgyver.core.command.Command;
import io.macgyver.core.script.ScriptExecutor;

import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLI {

	Logger logger = LoggerFactory.getLogger(CLI.class);
	String[] args;
	CommandLine commandLine = null;

	public CLI(String[] args) {
		this.args = args;
		Kernel.getExecutionProfile();
	}

	public static void main(String[] args) throws Exception {

		CLI cli = new CLI(args);
		cli.parseArgs();
		cli.run();

	}

	public String getCommand() {
		return (String) commandLine.getArgList().get(0);
	}

	public CommandLine getCommandLine() {
		return commandLine;
	}

	protected void parseArgs() throws ParseException {
		Options opts = new Options();

		// opts.addOption("s", "script", true, "script file");

		CommandLineParser clp = new BasicParser();
		CommandLine commandLine = clp.parse(opts, args);
		this.commandLine = commandLine;
	}

	public void run() {

		List<String> x = commandLine.getArgList();

		if (x.isEmpty()) {
			System.out.println("usage: mac <command>");
			System.exit(1);
		} else {
			String command = x.get(0);
			try {
				Command cmd = createCommand(command);
				cmd.execute(args);

			} catch (ClassNotFoundException e) {
				System.err.println("unknown command: " + command);
				System.exit(1);
			} catch (Exception e) {
				logger.warn("", e);
				System.exit(1);
			}
		}

	}

	Command createCommand(String name) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		String n = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		String className = Command.class.getPackage().getName() + "." + n
				+ "Command";
		logger.debug("command class: {}", className);

		Class<Command> clazz = (Class<Command>) Class.forName(className);
		return clazz.newInstance();
	}

	public void runScript(String scriptFile) {

		ScriptExecutor se = new ScriptExecutor();
		se.getBindings().put("kernel", Kernel.getInstance());
		se.run(scriptFile);

	}
}

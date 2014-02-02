package io.macgyver.core;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Simple wrapper to start server.
 * @author rschoening
 *
 */
public class ServerMain {

	public static void main(String [] args) throws Exception {
		List<String> argList = Lists.newArrayList(args);
		argList.add(0, "serve");
		CLI.main(argList.toArray(new String[0]));
	}
}

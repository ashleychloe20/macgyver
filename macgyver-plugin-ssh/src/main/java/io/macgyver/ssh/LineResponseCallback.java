package io.macgyver.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

import net.schmizz.sshj.connection.channel.direct.Session.Command;

public class LineResponseCallback extends ResponseCallback<Iterable<String>> {

	@Override
	public Iterable<String> handle(Command cmd) throws IOException {
		InputStream inputStream = cmd.getInputStream();
		ArrayList<String> list = Lists.newArrayList();
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream, "UTF-8"));
		while ((line = br.readLine()) != null) {
			list.add(line);
		}

		cmd.join(5, TimeUnit.SECONDS);

		if (cmd.getExitStatus() != 0) {
			String x = CharStreams.toString(new InputStreamReader(cmd
					.getErrorStream(), "UTF-8"));
			throw new SshException(cmd.getExitStatus(), x);

		}
		return list;
	}

}

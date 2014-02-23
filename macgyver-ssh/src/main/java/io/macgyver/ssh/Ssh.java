package io.macgyver.ssh;

import java.io.IOException;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

public class Ssh {

	private String username = System.getProperty("user.name");
	private String host;
	private String command;

	public static Ssh begin() {
		return new Ssh();
	}

	public Ssh username(String username) {
		this.username = username;
		return this;
	}

	public Ssh host(String host) {
		this.host = host;
		return this;
	}

	public Ssh command(String command) {
		this.command = command;
		return this;
	}

	public String execStringResponse() throws IOException {
		return exec(new StringResponseCallback());
	}

	public <T> T exec(ResponseCallback<T> cb) throws IOException {

		final SSHClient ssh = new SSHClient();
		ssh.loadKnownHosts();
		ssh.connect(host);
		try {
			ssh.authPublickey(username);
			final Session session = ssh.startSession();
			try {
				final Command cmd = session.exec(command);
				return cb.handle(cmd);

			} finally {
				session.close();
			}
		} finally {
			ssh.disconnect();
		}

	}
}

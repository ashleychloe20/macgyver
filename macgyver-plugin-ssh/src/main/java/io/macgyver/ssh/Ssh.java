/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.ssh;

import java.io.IOException;
import java.security.PublicKey;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

public class Ssh {

	private String username = System.getProperty("user.name");
	private String host;
	private String command;

	private HostKeyVerifier verifier = null;
	private int connectTimeout = -1;

	public Ssh username(String username) {
		this.username = username;
		return this;
	}

	public static Ssh to(String host) {
		return hostname(host);
	}
	public static Ssh hostname(String host) {
		Ssh ssh = new Ssh();
		ssh.host = host;
		return ssh;
	}

	public Ssh command(String command) {
		this.command = command;
		return this;
	}

	public Iterable<String> exec() throws IOException {
		return exec(new LineResponseCallback());
	}

	public String execString() throws IOException {
		return exec(new StringResponseCallback());
	}

	public Ssh connectTimeout(int timeout) {
		this.connectTimeout = timeout;
		return this;
	}

	public Ssh hostKeyVerifier(HostKeyVerifier v) {
		this.verifier = v;
		return this;
	}

	public Ssh disableHostKeyVerification() {
		verifier = new HostKeyVerifier() {

			@Override
			public boolean verify(String hostname, int port, PublicKey key) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		return this;
	}

	public <T> T exec(ResponseCallback<T> cb) throws IOException {

		final SSHClient ssh = new SSHClient();
		ssh.loadKnownHosts();

		if (verifier != null) {
			ssh.addHostKeyVerifier(verifier);
		}
		ssh.connect(host);
		if (connectTimeout > 0) {
			ssh.setConnectTimeout(connectTimeout);
		}
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

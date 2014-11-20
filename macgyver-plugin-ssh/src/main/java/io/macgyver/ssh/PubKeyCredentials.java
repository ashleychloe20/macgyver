package io.macgyver.ssh;

import java.io.File;

import com.google.common.base.Optional;

public class PubKeyCredentials implements Credentials {

	String username;
	Optional<File> privateKey = Optional.absent();

	public PubKeyCredentials(String username) {
		this.username = username;
		this.privateKey = locateKeyFile(username);
	}

	public PubKeyCredentials(String username, File pkf) {
		this.username = username;
		this.privateKey = Optional.fromNullable(pkf);
	}

	@Override
	public String getUsername() {
		return username;
	}

	public Optional<File> getPrivateKeyFile() {
		return privateKey;
	}

	private Optional<File> locateKeyFile(String username) {
		String home = System.getProperty("user.home",".");
		File f = new File(home, ".ssh/id_rsa");
		if (f.exists()) {
			return Optional.of(f);
		}

		f = new File(home, ".ssh/id_dsa");
		if (f.exists()) {
			return Optional.of(f);
		}

		return Optional.absent();
	}
}

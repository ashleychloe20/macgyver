package io.macgyver.core;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class FileConfigStore extends ConfigStore {

	File sourceFile;

	ConfigObject rootConfig = null;

	public FileConfigStore(File f) {
		this.sourceFile = f;
	}

	ConfigObject load() throws IOException {
		if (rootConfig != null) {
			return rootConfig;
		}

		ConfigSlurper cs = new ConfigSlurper();

		String profile = Kernel.getExecutionProfile();
		if (!Strings.isNullOrEmpty(profile)) {
			cs.setEnvironment(profile);
		}

		try (FileReader fr = new FileReader(sourceFile)) {

			rootConfig = cs.parse(sourceFile.toURI().toURL());
			return rootConfig;
		} finally {

		}

	}

	public Optional<ConfigObject> getRootConfigObject() {
		try {

			ConfigObject co = load();
			
			return Optional.fromNullable(co);

		} catch (IOException e) {
			throw new MacGyverException(e);

		}

	}

}

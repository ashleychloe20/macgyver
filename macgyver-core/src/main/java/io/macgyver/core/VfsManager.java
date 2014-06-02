package io.macgyver.core;

import java.io.File;

import com.google.common.base.Objects;

public class VfsManager {


	private File configLocation;
	private File scriptsLocation;
	private File dataLocation;
	private File webLocation;

	public VfsManager(File configLocation, File scriptsLocation,
			File dataLocation, File webLocation) {
		this.configLocation = configLocation;
		this.scriptsLocation = scriptsLocation;
		this.dataLocation = dataLocation;
		this.webLocation = webLocation;
	}

	public File resolveConfig(String n) {
		return new File(getConfigLocation(),n);
	}
	public File getConfigLocation() {
		return configLocation;
	}

	public File resolveScript(String n) {
		return new File(getScriptsLocation(),n);
	}
	public File getScriptsLocation() {
		return scriptsLocation;
	}

	public File getDataLocation() {
		return dataLocation;
	}

	public File resolveWeb(String n) {
		return new File(getWebLocation(),n);
	}
	public File getWebLocation() {
		return webLocation;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("dataLocation", dataLocation)
				.add("scriptsLocation", scriptsLocation)
				.add("configLocation", configLocation).toString();
	}

	
}

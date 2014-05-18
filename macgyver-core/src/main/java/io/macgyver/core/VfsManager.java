package io.macgyver.core;

import org.apache.commons.vfs2.FileObject;

import com.google.common.base.Objects;

public class VfsManager {

	private FileObject configLocation;
	private FileObject scriptsLocation;
	private FileObject dataLocation;
	private FileObject webLocation;

	public VfsManager(FileObject configLocation, FileObject scriptsLocation,
			FileObject dataLocation, FileObject webLocation) {
		this.configLocation = configLocation;
		this.scriptsLocation = scriptsLocation;
		this.dataLocation = dataLocation;
		this.webLocation = webLocation;
	}

	public FileObject getConfigLocation() {
		return configLocation;
	}

	public FileObject getScriptsLocation() {
		return scriptsLocation;
	}

	public FileObject getDataLocation() {
		return dataLocation;
	}

	public FileObject getWebLocation() {
		return webLocation;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("dataLocation", dataLocation)
				.add("scriptsLocation", scriptsLocation)
				.add("configLocation", configLocation).toString();
	}
}

package io.macgyver.core.resource;

import java.io.IOException;
import java.io.InputStream;


public abstract class Resource {

	ResourceLoader resourceLoader;
	String name;
	public Resource(ResourceLoader loader, String name) {
		this.resourceLoader = loader;
		this.name=name;
	}
	public String getVirtualName() throws IOException{
		return name;
	}
	public abstract InputStream openInputStream() throws IOException;
	public abstract boolean exists() ;
	public abstract String getSha1() throws IOException;
}

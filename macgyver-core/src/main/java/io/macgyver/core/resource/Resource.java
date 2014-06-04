package io.macgyver.core.resource;

import java.io.IOException;
import java.io.InputStream;


public abstract class Resource {

	ResourceProvider resourceLoader;
	String name;
	public Resource(ResourceProvider loader, String name) {
		this.resourceLoader = loader;
		this.name=name;
	}
	public ResourceProvider getResourceProvider() {
		return resourceLoader;
	}
	public String getPath() throws IOException{
		return name;
	}
	public abstract InputStream openInputStream() throws IOException;
	public abstract boolean exists() ;
	public abstract String getHash() throws IOException;
	public abstract String getContentAsString() throws IOException;
}

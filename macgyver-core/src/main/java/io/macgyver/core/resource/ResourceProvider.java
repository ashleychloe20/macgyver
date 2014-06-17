package io.macgyver.core.resource;

import java.io.IOException;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public abstract class ResourceProvider {

	String prefix=null;
	
	
	public abstract Iterable<Resource> findResources(ResourceMatcher rm) throws IOException;
	public final Iterable<Resource> findResources() throws IOException {
		return findResources(null);
	}
	public Optional<Resource> findResourceByHash(String path) throws IOException {
		return Optional.absent();
	}
	public Optional<Resource> findResourceByPath(String path) throws IOException {
		try {
			return Optional.fromNullable(getResourceByPath(path));
		}
		catch (IOException e) {
			
		}
		return Optional.absent();
	}
	
	public abstract Resource getResourceByPath(String path) throws IOException;
	
	
	public String removePrefix(String input) {
		if (input==null || prefix==null) {
			return input;
		}
		if (input.startsWith(prefix+"/")) {
			return input.substring(prefix.length()+1);
		}
		return input;
	}
	public String addPrefix(String input) {
		if (Strings.isNullOrEmpty(prefix)) {
			return input;
		}
		else {
			return prefix+"/"+input;
		}
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getPrefix() {
		return prefix;
	}
	
	public abstract void refresh() throws IOException;
}

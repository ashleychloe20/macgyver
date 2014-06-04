package io.macgyver.core.resource;

import java.io.IOException;

import com.google.common.base.Strings;

public abstract class ResourceProvider {

	String prefix=null;
	
	public abstract Iterable<Resource> findFileResources() throws IOException;

	public abstract Resource getResource(String path) throws IOException;
	
	
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
}

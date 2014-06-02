package io.macgyver.core.resource;

import java.io.IOException;

public abstract class ResourceLoader {

	public abstract Iterable<Resource> findFileResources() throws IOException;

	public abstract Resource getResource(String path) throws IOException;
}

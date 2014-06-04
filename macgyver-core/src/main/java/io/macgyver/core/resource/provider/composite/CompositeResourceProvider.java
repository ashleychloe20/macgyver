package io.macgyver.core.resource.provider.composite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceProvider;

public class CompositeResourceProvider extends ResourceProvider {

	Logger logger = LoggerFactory.getLogger(CompositeResourceProvider.class);
	List<ResourceProvider> resourceLoaders = Lists.newCopyOnWriteArrayList();

	public CompositeResourceProvider() {

	}

	public void addResourceLoader(ResourceProvider loader) {
		Preconditions.checkNotNull(loader);
		if (resourceLoaders.contains(loader)) {
			logger.warn("resource loader already registered: {}", loader);
		}
		resourceLoaders.add(loader);
	}

	@Override
	public Iterable<Resource> findFileResources() throws IOException {
		List<Resource> list = Lists.newArrayList();

		for (ResourceProvider loader : resourceLoaders) {

			Iterables.addAll(list, loader.findFileResources());

		}

		return list;
	}

	@Override
	public Resource getResource(String path) throws IOException {
		for (ResourceProvider loader : resourceLoaders) {

			try {
				Resource r = loader.getResource(path);
				if (r != null) {
					return r;
				}
			} catch (FileNotFoundException e) {

			}

		}
		throw new FileNotFoundException("resource not found: "+path);

	}

}

package io.macgyver.core.resource.provider.composite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceMatcher;
import io.macgyver.core.resource.ResourceProvider;

public class CompositeResourceProvider extends ResourceProvider {

	Logger logger = LoggerFactory.getLogger(CompositeResourceProvider.class);
	List<ResourceProvider> resourceLoaders = Lists.newCopyOnWriteArrayList();

	public CompositeResourceProvider() {

	}

	public void replaceResourceProvider(ResourceProvider rp) {
		logger.info("removing all providers of {}",rp.getClass());
		List<ResourceProvider> temp = Lists.newArrayList();
		for (ResourceProvider p: resourceLoaders) {
			if (rp.getClass().isAssignableFrom(p.getClass())) {
			
				temp.add(p);
			}
		}
		resourceLoaders.removeAll(temp);
		
		addResourceLoader(rp);
	}
	public void addResourceLoader(ResourceProvider loader) {
		Preconditions.checkNotNull(loader);
		if (resourceLoaders.contains(loader)) {
			logger.warn("resource loader already registered: {}", loader);
			return;
		}
		resourceLoaders.add(loader);
	}

	@Override
	public Iterable<Resource> findResources(ResourceMatcher matcher) throws IOException {
		List<Resource> list = Lists.newArrayList();

		for (ResourceProvider loader : resourceLoaders) {

			Iterables.addAll(list, loader.findResources(matcher));

		}

		return list;
	}

	@Override
	public Resource getResourceByPath(String path) throws IOException {
		for (ResourceProvider loader : resourceLoaders) {

			try {
				Resource r = loader.getResourceByPath(path);
				if (r != null) {
					return r;
				}
			} catch (FileNotFoundException e) {

			}

		}
		throw new FileNotFoundException("resource not found: "+path);

	}

	@Override
	public void refresh() throws IOException {
		for (ResourceProvider p: resourceLoaders) {
			p.refresh();
		}
		
	}

	
	public Optional<Resource> findResourceByHash(String hash) throws IOException {
		Preconditions.checkNotNull(hash);
		for (Resource r: findResources()) {

			if (r.getHash().equals(hash)) {
				return Optional.of(r);
			}
		}
		return Optional.absent();
	}
}

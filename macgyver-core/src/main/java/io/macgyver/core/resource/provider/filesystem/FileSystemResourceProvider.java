package io.macgyver.core.resource.provider.filesystem;

import io.macgyver.core.Bootstrap;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceMatcher;
import io.macgyver.core.resource.ResourceProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.google.common.io.Files;

public class FileSystemResourceProvider extends ResourceProvider {
	Logger logger = LoggerFactory.getLogger(FileSystemResourceProvider.class);
	File rootDir;

	public FileSystemResourceProvider(File rootDir) {
		this.rootDir = rootDir;
	}

	public boolean isApprovedPath(File f) {
		try {
			File approvedParent = Bootstrap.getInstance().getScriptsDir()
					.getCanonicalFile();
			while (f != null) {
				f = f.getCanonicalFile();
				if (f.equals(approvedParent)) {
					return true;
				}
				f = f.getParentFile();
			}
		} catch (Exception e) {
			logger.warn("", e);
		}
		return false;
	}

	@Override
	public Iterable<Resource> findResources(ResourceMatcher matcher)
			throws IOException {
		Preconditions.checkNotNull(matcher);
		List<Resource> tmp = Lists.newArrayList();
		TreeTraverser<File> tt = Files.fileTreeTraverser();
		String rootPath = rootDir.getCanonicalPath();
		for (File f : tt.breadthFirstTraversal(rootDir.getCanonicalFile())) {
			if (f.isFile() && isApprovedPath(f)) {
				String canonicalPath = f.getCanonicalPath();

				String virtualPath = canonicalPath;
				if (canonicalPath.startsWith(rootPath)) {
					virtualPath = canonicalPath.substring(rootPath.length());
				}
				while (virtualPath.startsWith("/")) {
					virtualPath = virtualPath.substring(1);
				}
				virtualPath = removePrefix(virtualPath);
				FileSystemResource fsr = new FileSystemResource(this,
						virtualPath, f.getCanonicalFile());
				if (matcher.matches(fsr)) {
					tmp.add(fsr);
				}
			}
		}

		return tmp;
	}

	@Override
	public Resource getResourceByPath(String path) throws IOException {
		String translatedPath = addPrefix(path);
		File f = new File(rootDir, translatedPath);
		logger.info("looking for " + f);
		if (!f.exists()) {
			throw new FileNotFoundException(f.getAbsolutePath());
		}
		FileSystemResource fsr = new FileSystemResource(this, path, f);
		return fsr;
	}

	@Override
	public void refresh() {
		// no-op
	}

}

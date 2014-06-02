package io.macgyver.core.resource.provider.filesystem;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.google.common.io.Files;

public class FileSystemResourceLoader extends ResourceLoader {

	File rootDir;

	public FileSystemResourceLoader(File rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public Iterable<Resource> findFileResources() throws IOException {

		List<Resource> tmp = Lists.newArrayList();
		TreeTraverser<File> tt = Files.fileTreeTraverser();
		String rootPath = rootDir.getCanonicalPath();
		for (File f : tt.breadthFirstTraversal(rootDir.getCanonicalFile())) {
			if (f.isFile()) {
				String canonicalPath = f.getCanonicalPath();

				String virtualPath = canonicalPath;
				if (canonicalPath.startsWith(rootPath)) {
					virtualPath = canonicalPath.substring(rootPath.length());
				}
				while (virtualPath.startsWith("/")) {
					virtualPath = virtualPath.substring(1);
				}
				FileSystemResource fsr = new FileSystemResource(this,
						virtualPath, f.getCanonicalFile());
				tmp.add(fsr);
			}
		}

		return tmp;
	}

	@Override
	public Resource getResource(String path) throws IOException {
		File f = new File(rootDir,path);
		if (!f.exists()) {
			throw new FileNotFoundException(f.getAbsolutePath());
		}
		FileSystemResource fsr = new FileSystemResource(this, path, f);
		return fsr;
	}

}

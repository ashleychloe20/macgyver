package io.macgyver.core.resource.provider.filesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Objects;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceLoader;
import io.macgyver.core.util.HashUtils;

public class FileSystemResource extends Resource {

	File fileObject;
	public FileSystemResource(ResourceLoader loader, String name, File object) {
		super(loader, name);
		this.fileObject = object;
	}

	
	public String toString() {
		try {
		return Objects.toStringHelper(this).add("virtualName", getVirtualName()).toString();
		}
		catch(Exception e) {
			return Objects.toStringHelper(this).toString();
		}
	}


	@Override
	public String getSha1() throws IOException {
		return Files.hash(fileObject, Hashing.sha1()).toString();
		
	}

	public InputStream openInputStream() throws IOException {
		return new BufferedInputStream(new FileInputStream(fileObject));
	}

	@Override
	public boolean exists() {
		return fileObject.exists();
	}
}

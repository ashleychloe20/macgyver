package io.macgyver.core.resource.provider.filesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceProvider;
import io.macgyver.core.util.HashUtils;

public class FileSystemResource extends Resource {

	File fileObject;

	public FileSystemResource(ResourceProvider loader, String name, File object) {
		super(loader, name);
		this.fileObject = object;
	}

	public String toString() {
		try {
			return Objects.toStringHelper(this).add("path", getPath())
					.toString();
		} catch (Exception e) {
			return Objects.toStringHelper(this).toString();
		}
	}

	@Override
	public String getHash() throws IOException {
		return Files.hash(fileObject, Hashing.sha1()).toString();

	}

	public String getContentAsString() throws IOException {
		String text;
		try (InputStreamReader reader = new InputStreamReader(
				openInputStream(), Charsets.UTF_8)) {
			text = CharStreams.toString(reader);
		}
		return text;
	}

	public InputStream openInputStream() throws IOException {
		return new BufferedInputStream(new FileInputStream(fileObject));
	}

	@Override
	public boolean exists() {
		return fileObject.exists();
	}
}

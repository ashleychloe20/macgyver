package io.macgyver.plugin.git;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.CharStreams;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceProvider;

public class GitResourceImpl extends Resource {

	ObjectId id;

	public GitResourceImpl(ResourceProvider loader, ObjectId objectId,
			String name) {
		super(loader, name);
		this.id = objectId;


	}

	protected GitResourceProvider getGitResourceProvider() {
		return (GitResourceProvider) getResourceProvider();
	}

	@Override
	public InputStream openInputStream() throws IOException {
		GitResourceProvider p = getGitResourceProvider();
		ObjectLoader loader = p.repo.open(id, Constants.OBJ_BLOB);
		return loader.openStream();

	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public String getHash() throws IOException {

		return id.getName();
	}

	@Override
	public String getContentAsString() throws IOException {
		try (InputStreamReader reader = new InputStreamReader(
				openInputStream(), Charsets.UTF_8)) {
			String text = CharStreams.toString(reader);
			return text;
		}
	}

	public String toString() {
		try {
			return Objects.toStringHelper(this).add("objectId", id)
					.add("name", getPath()).toString();
		} catch (Exception e) {
			return Objects.toStringHelper(this).toString();
		}
	}
}

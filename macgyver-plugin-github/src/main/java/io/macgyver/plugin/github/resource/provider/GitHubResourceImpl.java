package io.macgyver.plugin.github.resource.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.kohsuke.github.GHContent;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.CharStreams;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceProvider;

public class GitHubResourceImpl extends Resource {

		GHContent content;
		
	public GitHubResourceImpl(ResourceProvider loader, GHContent c, String name) {
		super(loader, name);
		this.content = c;
	
	}

	@Override
	public InputStream openInputStream() throws IOException {
		
		return new ByteArrayInputStream(content.getContent().getBytes());
	}

	@Override
	public boolean exists() {
		// should always exist
		return true;
	}
	public String getContentAsString() throws IOException {
		

		return content.getContent();
	}
	@Override
	public String getHash() throws IOException {
		return content.getSha();
	}



	public String toString() {
		try {
		return Objects.toStringHelper(this).add("name", getPath()).add("sha1", getHash()).toString();
		} 
		catch (IOException e) {
			return java.util.Objects.toString(this).toString();
		}
	}
}

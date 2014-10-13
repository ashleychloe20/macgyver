/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

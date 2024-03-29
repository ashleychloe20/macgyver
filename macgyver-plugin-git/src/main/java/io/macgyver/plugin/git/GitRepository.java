package io.macgyver.plugin.git;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class GitRepository {


		String url;
		String username;
		String password;
		
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
		
		public Git cloneInto(File dir, boolean isBare) throws GitAPIException {
			Preconditions.checkNotNull(dir);
			Preconditions.checkArgument(dir.exists(),"dir does not exist: "+dir.getAbsolutePath());
			
			CloneCommand cc = Git.cloneRepository().setURI(url).setBare(isBare)
					.setDirectory(dir).setCloneAllBranches(true);

			if ((!Strings.isNullOrEmpty(username))
					|| (!Strings.isNullOrEmpty(password))) {
				cc = cc.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
						Strings.nullToEmpty(username), Strings
								.nullToEmpty(password)));
			}
			Git git = cc.call();
			return git;
			
		}

}

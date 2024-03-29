package io.macgyver.plugin.github.resource.provider;

import java.io.IOException;
import java.util.List;

import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceMatcher;
import io.macgyver.core.resource.ResourceProvider;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.GitHubService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRef;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.google.common.collect.Lists;

public class GitHubResourceProvider extends ResourceProvider {

	org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GitHubResourceProvider.class);
	
	String repoName;
	String ref = "refs/heads/master";
	String rootPath = "";

	GitHub gitHub = null;

	public GitHubResourceProvider(GitHub gh) {
		this.gitHub = gh;
	}

	protected GitHub getGitHub() {
		return gitHub;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		if (rootPath==null) {
			rootPath = "";
		}
		while (rootPath.startsWith("/")) {
			rootPath = rootPath.substring(1);
		}
		this.rootPath = rootPath;
	}

	
	@Override
	public Iterable<Resource> findResources(ResourceMatcher matcher) throws IOException {
		GitHub gh = getGitHub();
		List<Resource> list = Lists.newArrayList();
		
		
		GHRepository repo = gh.getRepository(getRepoName());
		GHContent c;
	
		RepositoryService rs;
		Repository repox;
	
		for (GHRef ref: repo.getRefs()) {
			logger.debug("ref type: {}",ref.getObject().getType());
		}
		
		addAll(repo,list,getRef(),getRootPath());
	
		throw new UnsupportedOperationException("incomplete");
	}

	protected void addAll(GHRepository repo, List<Resource> list, String ref, String name) throws IOException {
		
		 List<GHContent> x = repo
					.getDirectoryContent(name, ref);
		for (GHContent c : x) {
			if (c.isFile()) {
				list.add(new GitHubResourceImpl(this, c,join(name,c.getName())));
			}
			else {
			
				addAll(repo, list,ref, join(name,c.getName()));
			}
		}	
	}
	@Override
	public Resource getResourceByPath(String path) throws IOException {
		throw new UnsupportedOperationException();
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}


	
	protected String join(String a, String b) {
		while (a.endsWith("/")) {
			a= a.substring(0,a.length()-1);
		}
		while (b.startsWith("/")) {
			b=b.substring(1);
		}
		if (a.equals("")) {
			return b;
		}
		return a+"/"+b;
	}

	@Override
	public void refresh() {
		// do nothing
		
	}
}

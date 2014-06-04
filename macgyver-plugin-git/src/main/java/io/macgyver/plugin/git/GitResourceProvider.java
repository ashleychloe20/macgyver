package io.macgyver.plugin.git;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectStream;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import io.macgyver.core.Kernel;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceProvider;
import io.macgyver.core.service.ServiceRegistry;

public class GitResourceProvider extends ResourceProvider {

	Logger logger = LoggerFactory.getLogger(GitResourceProvider.class);

	Optional<GitRepository> gitRepository = Optional.absent();

	Git git;
	Repository repo;

	String ref = "refs/heads/master";

	Optional<String> serviceName = Optional.absent();

	protected GitResourceProvider() {
		
	}
	public GitResourceProvider(String url, String username, String password) {
		GitRepository gitRepository = new GitRepository();
		gitRepository.setUrl(url);
		gitRepository.setUsername(username);
		gitRepository.setPassword(password);
		this.gitRepository = Optional.of(gitRepository);
	}

	public GitResourceProvider(String url) {
		this(url,null,null);
	}
	
	public static GitResourceProvider forGitRepositoryService(String name) {
		GitResourceProvider p = new GitResourceProvider();
		p.serviceName = Optional.of(name);
		return p;
	}
	public GitRepository getGitRepository() {
		if (serviceName.isPresent()) {
			ServiceRegistry reg = Kernel.getInstance().getApplicationContext().getBean(ServiceRegistry.class);
			return (GitRepository) reg.get(serviceName.get());
		}
		else {
			return gitRepository.get();
		}
		
	}

	public String getRef() {
		return ref;
	}

	public ObjectId resolveRef(String ref) throws IOException {
		Preconditions.checkNotNull(ref);
		ensureLocalClone();
		return repo.resolve(ref);
	}

	public void selectRef(String ref) throws IOException {
		ObjectId id = resolveRef(ref);
		if (id == null) {
			throw new IllegalArgumentException("could not resolve ref: " + ref);
		}

	}

	public synchronized void ensureLocalClone() throws IOException {
		GitRepository gitRepository = getGitRepository();
		try {
			if (git != null) {
				return;
			}
			File dir = Files.createTempDir();

			logger.info("cloning {} into {}", gitRepository.getUrl(), dir);

			CloneCommand cc = Git.cloneRepository()
					.setURI(gitRepository.getUrl()).setBare(true)
					.setDirectory(dir).setCloneAllBranches(true);

			if ((!Strings.isNullOrEmpty(gitRepository.getUsername()))
					|| (!Strings.isNullOrEmpty(gitRepository.getPassword()))) {
				cc = cc.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
						Strings.nullToEmpty(gitRepository.getUsername()),
						Strings.nullToEmpty(gitRepository.getPassword())));
			}
			git = cc.call();
			repo = git.getRepository();

		} catch (GitAPIException e) {
			throw new IOException(e);
		}

	}

	public synchronized void fetch() throws IOException {
		GitRepository gitRepository = getGitRepository();
		try {
			ensureLocalClone();
			FetchCommand fc = git.fetch();

			if ((!Strings.isNullOrEmpty(gitRepository.getUsername()))
					|| (!Strings.isNullOrEmpty(gitRepository.getPassword()))) {
				fc = fc.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
						Strings.nullToEmpty(gitRepository.getUsername()),
						Strings.nullToEmpty(gitRepository.getPassword())));
			}
			fc.call();
		} catch (GitAPIException e) {
			throw new IOException(e);
		}

	}

	@Override
	public Iterable<Resource> findFileResources() throws IOException {
		ensureLocalClone();
		ObjectId headCommit = repo.resolve(getRef());

		if (headCommit == null) {
			throw new IOException("ref not found: " + getRef());
		}
		TreeWalk tw = null;
		RevWalk rw = new RevWalk(repo);
		try {
			RevCommit rc = rw.parseCommit(headCommit);

			RevTree tree = rc.getTree();

			tw = new TreeWalk(repo);
			tw.addTree(tree);
			tw.setRecursive(true);
			List<Resource> list = Lists.newArrayList();
			while (tw.next()) {

				GitResourceImpl gri = new GitResourceImpl(this,
						tw.getObjectId(0), tw.getPathString());

				list.add(gri);

			}
			return list;
		} finally {
			if (tw != null) {
				tw.release();
			}
			if (rw != null) {
				rw.release();
			}
		}

	}

	@Override
	public Resource getResource(String path) throws IOException {

		ensureLocalClone();
		TreeWalk tw = null;
		RevWalk rw = null;
		try {
			ObjectId headCommit = repo.resolve(getRef());
			rw = new RevWalk(repo);

			tw = TreeWalk.forPath(repo, path, rw.parseCommit(headCommit)
					.getTree());
			if (tw == null) {
				throw new FileNotFoundException(path + " not found in ref "
						+ getRef() + " (" + headCommit.getName() + ")");
			}
			ObjectId id = tw.getObjectId(0);
			GitResourceImpl gri = new GitResourceImpl(this, id, path);
			return gri;
		} finally {
			if (tw != null) {
				tw.release();
			}
			if (rw != null) {
				rw.release();
			}
		}
	}

	public void close() {
		if (repo != null) {
			repo.close();
		}
		if (git != null) {
			git.close();
		}
	}
}

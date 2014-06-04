package io.macgyver.plugin.git;

import io.macgyver.test.MacGyverIntegrationTest;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class JGitTest extends MacGyverIntegrationTest {

	@Test
	public void testIt() throws IOException, GitAPIException {
		// http://stackoverflow.com/questions/6861881/jgit-cannot-find-a-tutorial-or-simple-example

		/*
		 * SshSessionFactory factory = new JschConfigSessionFactory() {
		 * 
		 * 
		 * 
		 * public void configure(Host hc, Session session) {
		 * session.setConfig("StrictHostKeyChecking", "no"); }
		 * 
		 * 
		 * };
		 * 
		 * 
		 * FileRepositoryBuilder rb = new FileRepositoryBuilder();
		 * 
		 * 
		 * File tempDir = new
		 * File("./target/git-junit/"+UUID.randomUUID().toString());
		 * tempDir.mkdirs(); tempDir = tempDir.getAbsoluteFile();
		 * 
		 * 
		 * System.out.println(tempDir); rb = rb.setWorkTree(tempDir);
		 * 
		 * Git.cloneRepository() .setURI("git@github.com:if6was9/macgyver.git")
		 * .setDirectory(tempDir) .call();
		 * 
		 * 
		 * Repository r = rb.setWorkTree(tempDir).build();
		 * 
		 * Git g = new Git(r);
		 * 
		 * g.fetch().call();
		 * 
		 * RevWalk rw = new RevWalk(r, 100);
		 * 
		 * RevCommit rc =
		 * rw.parseCommit(r.resolve(org.eclipse.jgit.lib.Constants.HEAD));
		 * 
		 * System.out.println(rc);
		 * 
		 * g.pull().call();
		 */

	}

}

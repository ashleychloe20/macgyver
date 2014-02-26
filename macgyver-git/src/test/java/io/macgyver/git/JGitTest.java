package io.macgyver.git;

import io.macgyver.test.MacgyverIntegrationTest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.DepthWalk.RevWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.junit.Test;

import com.google.common.io.Files;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;




public class JGitTest extends MacgyverIntegrationTest {

	

	@Test
	public void testIt() throws IOException, GitAPIException {
		// http://stackoverflow.com/questions/6861881/jgit-cannot-find-a-tutorial-or-simple-example
		
		/*
		   SshSessionFactory factory = new JschConfigSessionFactory() {
			
		

		        public void configure(Host hc, Session session) {
		            session.setConfig("StrictHostKeyChecking", "no");
		        }

		       
		    };
		
		
		FileRepositoryBuilder rb = new FileRepositoryBuilder();
		
		
		File tempDir = new File("./target/git-junit/"+UUID.randomUUID().toString());
		tempDir.mkdirs();
		tempDir = tempDir.getAbsoluteFile();
		
		
		System.out.println(tempDir);
		rb = rb.setWorkTree(tempDir);
		
	       Git.cloneRepository() 
           .setURI("git@github.com:if6was9/macgyver.git")
           .setDirectory(tempDir)
           .call();  
	       
		
	       Repository r = rb.setWorkTree(tempDir).build();
	       
	       Git g = new Git(r);
	       
	       g.fetch().call();
	       
	       RevWalk rw = new RevWalk(r, 100);
	       
	       RevCommit rc = rw.parseCommit(r.resolve(org.eclipse.jgit.lib.Constants.HEAD));
	       
	       System.out.println(rc);
	       
	       g.pull().call();
	       
	
		*/

	
		
	}
	
}

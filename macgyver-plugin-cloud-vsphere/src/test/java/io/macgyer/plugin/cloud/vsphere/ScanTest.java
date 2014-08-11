package io.macgyer.plugin.cloud.vsphere;

import java.net.URL;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

import io.macgyver.plugin.cloud.vsphere.VmQueryTemplate;
import io.macgyver.test.MacGyverIntegrationTest;
import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
public class ScanTest extends MacGyverIntegrationTest {

	
	String url;
	String user;
	String pass;
	ServiceInstance serviceInstance;
	
	@Before
	public void setupCreds() {
		 url = getPrivateProperty("vcenter.url");
		 user = getPrivateProperty("vcenter.username");
		 pass = getPrivateProperty("vcenter.password");
		
		Assume.assumeFalse("url must be set",Strings.isNullOrEmpty(url));
		Assume.assumeFalse("username must be set",Strings.isNullOrEmpty(user));
		Assume.assumeFalse("password must be set",Strings.isNullOrEmpty(pass));
	
		
		try {
			serviceInstance = new ServiceInstance(new URL(url), user,pass,true);
		}
		catch (Exception e) {
			Assume.assumeTrue(false);
		}
	}
	
	@Test
	public void testX() throws Exception {
		
		VmQueryTemplate t = new VmQueryTemplate(serviceInstance);
		for (VirtualMachine vm : t.findAllVirtualMachines()) {
			System.out.println(vm);
		}
		
		
		
	}
	
	@Test
	@Ignore
	public void testY() throws Exception {
		
		//https://github.com/if6was9/jclouds-labs/blob/master/vsphere/src/test/java/org/jclouds/vsphere/ContextBuilderTest.java
	      ImmutableSet modules = ImmutableSet.of(new ExecutorServiceModule(sameThreadExecutor(), sameThreadExecutor()), new SshjSshClientModule());
	      ComputeServiceContext context = ContextBuilder.newBuilder("vsphere")
//	     
	              .credentials(user, pass)
	              .endpoint(url)
	              .modules(modules)
	              .buildView(ComputeServiceContext.class);
		
	      
	      for(ComputeMetadata md: context.getComputeService().listNodes()) {
	    	  System.out.println(md);
	      }
	      
	}
}

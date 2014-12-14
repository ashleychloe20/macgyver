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
package io.macgyver.plugin.cloud.vsphere.cmdb;

import java.net.URL;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

import io.macgyver.neorx.rest.NeoRxClient;
import io.macgyver.plugin.cloud.vsphere.VSphereQueryTemplate;
import io.macgyver.plugin.cloud.vsphere.cmdb.VSphereScanner;
import io.macgyver.test.MacGyverIntegrationTest;
import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
public class VSphereScannerIntegrationTest extends MacGyverIntegrationTest {

	
	String url;
	String user;
	String pass;
	ServiceInstance serviceInstance;
	
	@Autowired
	NeoRxClient neo4j;
	
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
			e.printStackTrace();
			Assume.assumeTrue(false);
		}
	}
	
	@Test
	public void testX() throws Exception {
		
		
		VSphereScanner s = new VSphereScanner(serviceInstance,neo4j);
		
	//	s.scanAllVirtualMachines();
		s.scanAllHosts();

		
		
		
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

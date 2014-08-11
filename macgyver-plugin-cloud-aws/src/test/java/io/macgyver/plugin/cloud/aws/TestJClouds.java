package io.macgyver.plugin.cloud.aws;

import java.util.Properties;
import java.util.Set;

import io.macgyver.test.MacGyverIntegrationTest;

import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.ec2.EC2ApiMetadata;
import org.jclouds.ec2.compute.EC2ComputeService;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.junit.Test;
import org.springframework.boot.logging.log4j.Log4JLoggingSystem;

import com.google.common.collect.ImmutableSet;

public class TestJClouds extends MacGyverIntegrationTest {

	@Test
	public void testX() {

		Properties overrides = new Properties();
	//	overrides.setProperty(Constants.PROPERTY_PROXY_HOST, "localhost");
	//	overrides.setProperty(Constants.PROPERTY_PROXY_PORT, "8888");
		overrides.setProperty(Constants.PROPERTY_TRUST_ALL_CERTS, "true");
		overrides.setProperty(Constants.PROPERTY_RELAX_HOSTNAME, "true");
		overrides.setProperty("jclouds.regions","us-west-1,us-west-2,us-east-1");
		ComputeServiceContext context = ContextBuilder
				.newBuilder("aws-ec2").overrides(overrides)
				.credentials(getPrivateProperty("aws.accessKey"),
						getPrivateProperty("aws.secretKey"))
				.modules(ImmutableSet.of(new SLF4JLoggingModule()))
				.buildView(ComputeServiceContext.class);

		// http://jclouds.apache.org/guides/aws/
		// https://github.com/jclouds/jclouds-examples/blob/master/compute-basics/src/main/java/org/jclouds/examples/compute/basics/MainApp.java
		
		  Set<? extends Location> locations =
		  context.getComputeService().listAssignableLocations(); for (Location
		  location: locations) { System.out.println("LOC: "+location); }
		 
		  
		ComputeService svc = context.getComputeService();
		
		for (ComputeMetadata cn : svc.listNodes()) {
			NodeMetadata nm = (NodeMetadata) cn;
			System.out.println(nm.getId());
			System.out.println(nm.getLocation());
			System.out.println(nm.getUserMetadata());
			System.out.println(nm.getPrivateAddresses());
			System.out.println(nm.getName());
		}

	}
}

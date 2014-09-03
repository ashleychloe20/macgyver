package io.macgyver.jclouds.vsphere;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class VSphereApiMetadataTest {

	
	@Test
	public void testIt() {
		VSphereApiMetadata md = new VSphereApiMetadata().toBuilder().defaultEndpoint("https://ssx").build();
		
		assertEquals("https://ssx",md.getDefaultEndpoint().get());
	}
	
	@Test
	public void testX() {
		VSphereApiMetadata md = new VSphereApiMetadata().toBuilder().credentialName("hello").build();
		
		assertEquals("hello", md.getCredentialName().get());
	}
}

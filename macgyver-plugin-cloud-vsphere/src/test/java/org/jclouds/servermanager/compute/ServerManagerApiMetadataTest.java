package org.jclouds.servermanager.compute;

import static org.junit.Assert.assertEquals;

import io.macgyver.jclouds.vsphere.VSphereApiMetadata;

import org.junit.Test;

public class ServerManagerApiMetadataTest {

	
	@Test
	public void testX() {
		VSphereApiMetadata md = new VSphereApiMetadata().toBuilder().credentialName("hello").build();
		
		assertEquals("hello", md.getCredentialName().get());
	}
}

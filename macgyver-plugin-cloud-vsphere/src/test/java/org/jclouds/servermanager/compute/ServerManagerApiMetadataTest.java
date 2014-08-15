package org.jclouds.servermanager.compute;

import static org.junit.Assert.assertEquals;

import io.macgyver.jclouds.vsphere.ServerManagerApiMetadata;

import org.junit.Test;

public class ServerManagerApiMetadataTest {

	
	@Test
	public void testX() {
		ServerManagerApiMetadata md = new ServerManagerApiMetadata().toBuilder().credentialName("hello").build();
		
		assertEquals("hello", md.getCredentialName().get());
	}
}

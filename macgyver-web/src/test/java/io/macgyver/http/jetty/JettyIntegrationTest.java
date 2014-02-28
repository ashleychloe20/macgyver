package io.macgyver.http.jetty;

import org.eclipse.jetty.http.MimeTypes;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import io.macgyver.test.MacgyverIntegrationTest;

public class JettyIntegrationTest extends MacgyverIntegrationTest {

	@Autowired
	@Qualifier("io.macgyver.web.MimeTypes")
	MimeTypes mimeTypes;
	
	
	@Test
	public void testMimeTypes() {
		
		Assert.assertEquals("image/png",mimeTypes.getMimeByExtension("test.png"));
	}
	
}

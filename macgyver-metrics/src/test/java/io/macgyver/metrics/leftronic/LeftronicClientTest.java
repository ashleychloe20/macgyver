package io.macgyver.metrics.leftronic;

import org.junit.Ignore;
import org.junit.Test;

public class LeftronicClientTest {

	
	@Test
	@Ignore
	public void testX() throws Exception {
		
		
		LeftronicClient c = new LeftronicClient("xxxx");
		
		c.send("sandboxTestStream", 150);
		
		
		Thread.sleep(5000);
	}
}

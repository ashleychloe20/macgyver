package io.macgyver.metrics.leftronic;

import org.junit.Ignore;
import org.junit.Test;

public class LeftronicClientTest {

	
	@Test
	@Ignore
	public void testX() throws Exception {
		
		
		Leftronic c = new Leftronic();
		c.setApiKey("xxx");
		
		c.record("sandboxTestStream", 150);
		
		
		Thread.sleep(5000);
	}
}

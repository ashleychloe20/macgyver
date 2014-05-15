package io.macgyver.scheduler;

import org.junit.Assert;
import org.junit.Test;

public class ScheduledScriptTest {

	
	@Test
	public void testScheduledScriptNullHandling() {
		ScheduledScript ss = new ScheduledScript(null, null);
		Assert.assertNotNull(ss.toString());
		
		ss.getJobKey();
	}
}

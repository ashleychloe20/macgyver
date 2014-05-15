package io.macgyver.core.scheduler;

import io.macgyver.core.scheduler.ScheduledScript;

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

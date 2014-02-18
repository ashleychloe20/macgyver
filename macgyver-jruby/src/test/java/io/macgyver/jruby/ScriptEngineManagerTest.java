package io.macgyver.jruby;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Assert;
import org.junit.Test;

public class ScriptEngineManagerTest {

	
	@Test
	public void testEngine() {
		ScriptEngineManager m = new ScriptEngineManager();
		
		
		Assert.assertNotNull(m.getEngineByExtension("rb"));
		Assert.assertNotNull(m.getEngineByName("ruby"));
		
	}
}

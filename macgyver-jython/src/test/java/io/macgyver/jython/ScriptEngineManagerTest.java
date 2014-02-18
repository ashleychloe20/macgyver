package io.macgyver.jython;

import javax.script.ScriptEngineManager;

import org.junit.Assert;
import org.junit.Test;

public class ScriptEngineManagerTest {

	
	
	@Test
	public void testEngine() {
		ScriptEngineManager m = new ScriptEngineManager();
		
		
		Assert.assertNotNull(m.getEngineByExtension("py"));
		Assert.assertNotNull(m.getEngineByName("python"));
		
	}
}

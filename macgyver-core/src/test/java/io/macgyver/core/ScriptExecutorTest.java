package io.macgyver.core;

import io.macgyver.core.script.ScriptExecutor;
import io.macgyver.test.MacGyverIntegrationTest;

import org.junit.Assert;
import org.junit.Test;

public class ScriptExecutorTest extends MacGyverIntegrationTest {

	
	@Test
	public void testExecTrivialGroovy() throws Exception {

		String script = "trivial.groovy";
		ScriptExecutor se = new ScriptExecutor();
		se.run(script);

		Assert.assertEquals(42,se.getBindings().get("lifeTheUniverseAndEverything"));
	}

	
	
	@Test
	public void testTrivialJavaScript() throws Exception {

		String script = "trivial.js";
		ScriptExecutor se = new ScriptExecutor();
		se.run(script);

		Assert.assertEquals(42.0,se.getBindings().get("lifeTheUniverseAndEverything"));
	}	
	
	

	
}

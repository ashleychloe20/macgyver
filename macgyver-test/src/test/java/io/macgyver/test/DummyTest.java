package io.macgyver.test;

import org.junit.Assert;
import org.junit.Test;
import org.kohsuke.randname.RandomNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyTest {

	Logger logger = LoggerFactory.getLogger(DummyTest.class);
	
	@Test
	public void testDummy() {
		RandomNameGenerator g = new RandomNameGenerator();
		Assert.assertNotEquals(g.next(), g.next());
		
		
		String s0 = new RandomNameGenerator(0).next();
		String s1 = new RandomNameGenerator(0).next();
		
		Assert.assertEquals(s0,s1);
		
	}
}

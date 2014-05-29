package io.macgyver.plugin.cmdb;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class AppInstanceComparatorTest {

	
	@Test
	public void testIt() {
		AppInstance a1 = new AppInstance("b","group","a");
		AppInstance a2 = new AppInstance("a","group","a");
		
		AppInstanceComparator c = new AppInstanceComparator();

		
		Assert.assertTrue(c.compare(a1, a2)>0);
		Assert.assertTrue(c.compare(a2, a1)<0);
	}
	
	
	
	@Test
	public void testMissingOnLeftSide() {
		AppInstance a1 = new AppInstance("b","group","a");
		AppInstance a2 = new AppInstance("a","group","a");
		
		
		AppInstanceComparator c = new AppInstanceComparator();
		
		
		Assert.assertTrue("myprop".compareTo("")>0);
		Assert.assertTrue(c.compare(a1, a2)>0);

	}

}

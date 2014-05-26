package io.macgyver.plugin.cmdb;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class AppInstanceComparatorTest {

	
	@Test
	public void testIt() {
		AppInstance a1 = new AppInstance("b","a");
		AppInstance a2 = new AppInstance("a","a");
		
		AppInstanceComparator c = new AppInstanceComparator();
		c.sortAscending("host");
		
		Assert.assertTrue(c.compare(a1, a2)>0);
		Assert.assertTrue(c.compare(a2, a1)<0);
	}
	
	@Test
	public void testMissing() {
		AppInstance a1 = new AppInstance("b","a");
		AppInstance a2 = new AppInstance("a","a");
		
		AppInstanceComparator c = new AppInstanceComparator();
		c.sortAscending("missing");
		
		Assert.assertTrue(c.compare(a1, a2)==0);
		Assert.assertTrue(c.compare(a2, a1)==0);
	}
	
	@Test
	public void testMissingOnLeftSide() {
		AppInstance a1 = new AppInstance("b","a");
		AppInstance a2 = new AppInstance("a","a");
		
		a1.getProperties().put("myprop", "a");
		
		AppInstanceComparator c = new AppInstanceComparator();
		c.sortAscending("myprop");
		
		Assert.assertTrue("myprop".compareTo("")>0);
		Assert.assertTrue(c.compare(a1, a2)>0);

	}
	@Test
	public void testMissingOnRightSide() {
		AppInstance a1 = new AppInstance("b","a");
		AppInstance a2 = new AppInstance("a","a");
		
		a2.getProperties().put("myprop", "a");
		
		AppInstanceComparator c = new AppInstanceComparator();
		c.sortAscending("myprop");
		
		Assert.assertTrue("".compareTo("myprop")<0);
		Assert.assertTrue(c.compare(a1, a2)<0);

	}
	
	@Test
	public void testComposite() {
		AppInstance a1 = new AppInstance("a","a");
		AppInstance a2 = new AppInstance("a","b");
		
		AppInstanceComparator c = new AppInstanceComparator();
		c.sortAscending("host");
		c.sortAscending("appId");
		
		Assert.assertTrue(c.compare(a1, a2)<0);
		Assert.assertTrue(c.compare(a2, a1)>0);

	}
	@Test
	public void testCompositeDescending() {
		AppInstance a1 = new AppInstance("a","a");
		AppInstance a2 = new AppInstance("a","b");
		
		AppInstanceComparator c = new AppInstanceComparator();
		c.sortDescending("host");
		c.sortDescending("appId");
		
		Assert.assertTrue(c.compare(a1, a2)>0);
		Assert.assertTrue(c.compare(a2, a1)<0);

	}
	@Test
	public void testCompositeEquals() {
		AppInstance a1 = new AppInstance("a","a");
		AppInstance a2 = new AppInstance("a","a");
		
		AppInstanceComparator c = new AppInstanceComparator();
		c.sortAscending("host");
		c.sortAscending("appId");
		
		Assert.assertTrue(c.compare(a1, a2)==0);
		Assert.assertTrue(c.compare(a2, a1)==0);

	}
	
	@Test
	public void testSortComposite() {
		AppInstance a0 = new AppInstance("z","a");
		AppInstance a1 = new AppInstance("a","a");
		AppInstance a2 = new AppInstance("a","ab");
		
		List<AppInstance> list = Lists.newArrayList(a0,a1,a2);
		
		AppInstanceComparator c = new AppInstanceComparator();
		c.sortAscending("host");
		c.sortAscending("appId");
		
		Collections.sort(list,c);
	
		Assert.assertSame(a0,list.get(2));
		Assert.assertSame(a1,list.get(0));
		Assert.assertSame(a2,list.get(1));

	}
}

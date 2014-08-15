package io.macgyver.core.web.w2ui;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Enums;
import com.google.common.base.Optional;

public class SortTest {

	
	@Test
	public void testIt() {
		
		assertEquals(Optional.of(Sort.Direction.ASCENDING),Enums.getIfPresent(Sort.Direction.class, "ASCENDING"));
	
		Assert.assertEquals(Sort.Direction.ASCENDING,Sort.Direction.fromString("asc"));
		Assert.assertEquals(Sort.Direction.DESCENDING,Sort.Direction.fromString("desc"));
		Assert.assertEquals(Sort.Direction.ASCENDING,Sort.Direction.valueOf("ASCENDING"));
		Assert.assertEquals(Sort.Direction.DESCENDING,Sort.Direction.valueOf("DESCENDING"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void valueOfFails() {
		
		// will not work
		Assert.assertEquals(Sort.Direction.ASCENDING,Sort.Direction.valueOf("asc"));
	
	}
}

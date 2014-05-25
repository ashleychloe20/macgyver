package io.macgyver.core.misc;

import java.util.Date;

import org.junit.Test;
import org.ocpsoft.prettytime.PrettyTime;

public class PrettyTimeTest {

	
	@Test
	public void testIt(){
		PrettyTime pt = new PrettyTime();
	
		String s = pt.format(new Date(System.currentTimeMillis()-200010));
		
	
	}
}

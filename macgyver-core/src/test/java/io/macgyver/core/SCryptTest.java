package io.macgyver.core;

import org.junit.Assert;
import org.junit.Test;

import com.lambdaworks.crypto.SCryptUtil;

public class SCryptTest {

	String crypt = "$s0$c0801$OnXRutmFsj1Kb5XN4DG5Lg==$5xm3q0bRQSf72BPOOfq4T5AW/FMZ0ta1350e8Ou18A8=";
	@Test
	public void testEncrypt() {
		//System.setProperty("com.lambdaworks.jni.loader", "nil");
		String out = SCryptUtil.scrypt("test", 4096, 8, 1);
		
	
		Assert.assertTrue(SCryptUtil.check("test", out));
	}
	
	@Test
	public void testDecrypt() {
		Assert.assertTrue(SCryptUtil.check("test", crypt));
	}
}

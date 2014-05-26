package io.macgyver.core.util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.macgyver.core.util.HashUtils;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.BaseEncoding;


public class HashUtilsTest {

	@Test
	public void testPattern() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte [] b = MessageDigest.getInstance("sha1").digest("/a/b".getBytes("UTF-8"));
		String hexVal = BaseEncoding.base16().lowerCase().encode(b);
		
		Assert.assertEquals(hexVal,HashUtils.calculateCompositeId("a","b"));
	}
	
	@Test
	public void testEncoding() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		Assert.assertEquals("79cc6964e1c99c9f4da834114b230f7694648b9d",HashUtils.calculateCompositeId("a","","b"));
		Assert.assertEquals("79cc6964e1c99c9f4da834114b230f7694648b9d",HashUtils.calculateCompositeId("a",null,"b"));
	}
	
	@Test
	public void testCaseSensitivity() {
		Assert.assertNotEquals(HashUtils.calculateCompositeId("Hello"),HashUtils.calculateCompositeId("hello"));
	}
}

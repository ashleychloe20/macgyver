package io.macgyver.core.crypto;

import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.macgyver.test.MacgyverIntegrationTest;

public class CryptoTest extends MacgyverIntegrationTest {

	@Autowired
	Crypto crypto;

	@Test
	public void testIt() throws GeneralSecurityException {
		String plainTextInput = "Hello!";
		String cipher1 = crypto.encryptString(plainTextInput, "mac0");

		String cipher2 = crypto.encryptString(plainTextInput, "mac0");
		Assert.assertFalse("encryption should be salted",cipher1.equals(cipher2));
		String plainTextOutput = crypto.decryptString(cipher1);
		Assert.assertEquals(plainTextInput, plainTextOutput);


	}
	
	@Test
	public void testDecryptWithMac0() throws GeneralSecurityException {
		String input = "{\"k\":\"mac0\",\"d\":\"XF3+l0ncJvioma7canvCe+NhHchjFqy7iGWm5/ymQgU=\"}";
		
		String plain = crypto.decryptString(input);
		
		Assert.assertEquals("abcdefg", plain);
		
		
	
	}
	
	@Test(expected=KeyStoreException.class)
	public void testInvalidKey() throws GeneralSecurityException {
		String cipherEnvelope = crypto.encryptString("test", "invalid");
	}
	@Test
	public void testEncrypt() throws GeneralSecurityException {
		String cipherEnvelope = crypto.encryptString("test", "mac0");
		JsonObject obj = Json.createReader(new StringReader(cipherEnvelope)).readObject();
		
		Assert.assertEquals("mac0",obj.getString("k"));
		Assert.assertTrue(obj.getString("d").length()>10);
		
	}
	
	@Test(expected=GeneralSecurityException.class)
	public void testFailedDecrypt1()  throws GeneralSecurityException {
		String x = Json.createObjectBuilder().add("k", "x").add("d", "x").build().toString();
		crypto.decryptString(x);
	}
	
	@Test(expected=GeneralSecurityException.class)
	public void testFailedDecrypt2()  throws GeneralSecurityException {
		String x = Json.createObjectBuilder().add("k", "x").build().toString();
		crypto.decryptString(x);
	}
	@Test
	public void testPassThrough() {
		String x = "pass me through";
		Assert.assertEquals(x,crypto.decryptStringWithPassThrough(x));
		
		// Even something that looks like an encrypted envelope should be passed through on failure
		x = Json.createObjectBuilder().add("k", "x").add("d", "x").build().toString();
		Assert.assertEquals(x,crypto.decryptStringWithPassThrough(x));
	}
}

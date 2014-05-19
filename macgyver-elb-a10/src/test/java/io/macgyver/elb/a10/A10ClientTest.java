package io.macgyver.elb.a10;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;

import io.macgyver.test.MacGyverIntegrationTest;

public class A10ClientTest extends MacGyverIntegrationTest {

	A10Client client;

	@Before
	public void setupClient() {
		String url = getPrivateProperty("a10.url");
		String username = getPrivateProperty("a10.username");
		String password = getPrivateProperty("a10.password");

		Assume.assumeTrue(!Strings.isNullOrEmpty(url));
		Assume.assumeTrue(!Strings.isNullOrEmpty(username));
		Assume.assumeTrue(!Strings.isNullOrEmpty(password));
		client = new A10Client(url, username, password);
		client.setCertificateVerificationEnabled(false);
	}

	@Test
	public void testAuthFailure() {

		try {
			client.setPassword("test");
			client.authenticate();
			Assert.fail("authentication should have failed");

		} catch (A10RemoteException e) {
			// Maybe we want to test the code/message?
			// io.macgyver.elb.a10.A10RemoteException: 520486915: Admin password
			// error
		}
	}

	@Test
	public void testTokenCache() {

		String t1 = client.getAuthToken();
		String t2 = client.getAuthToken();
		
		Assert.assertEquals(t1,t2);
		

	}
	
	@Test
	public void testTokenCacheDisabled() throws InterruptedException{
		client.setTokenCacheDuration(0, TimeUnit.MICROSECONDS);
		String t1 = client.getAuthToken();
		Thread.sleep(100);
		String t2 = client.getAuthToken();
	
		Assert.assertNotEquals(t1, t2);
		

	}
	
	@Test
	public void testSLB() {

		JsonObject obj = client.getAllSLB();
		

	}
}
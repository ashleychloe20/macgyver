package io.macgyver.core.auth;

import io.macgyver.test.MacGyverIntegrationTest;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mapdb.TxMaker;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class UserManagerTest extends MacGyverIntegrationTest {

	@Autowired
	UserManager userManager;

	@Autowired
	TxMaker txMaker;

	@Test
	public void testAutowire() {
		Assert.assertNotNull(userManager);
		Assert.assertNotNull(userManager.template);
	}
	@Test
	public void testUserManager() {
		Preconditions.checkNotNull(userManager);
		String username = UUID.randomUUID().toString();
		Optional<ObjectNode> x = userManager.getUserAsJsonObject(username);
		Assert.assertFalse(x.isPresent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveWithInvalidArg() {
		Preconditions.checkNotNull(userManager);
		ObjectNode u = new ObjectMapper().createObjectNode();
		userManager.save(u);
	}

	@Test
	public void testAuthenticateFailureWithNoPassword() {
		String username = "user_" + UUID.randomUUID().toString();
		ObjectNode a = new ObjectMapper().createObjectNode();
		a.put("username", username);
		userManager.save(a);

		Assert.assertFalse(userManager.authenticate(username, "xxx"));
	}

	@Test
	public void testAuthentication() {
		String username = "user_" + UUID.randomUUID().toString();
		ObjectNode a = new ObjectMapper().createObjectNode();
		a.put("username", username);
		userManager.save(a);

		String pwd = UUID.randomUUID().toString();
		userManager.setPassword(username, pwd);
		Assert.assertTrue(userManager.authenticate(username, pwd));
		Assert.assertFalse(userManager.authenticate(username, pwd + "XXX"));
		Assert.assertFalse(userManager.authenticate(username, ""));
		Assert.assertFalse(userManager.authenticate(null, null));
		Assert.assertFalse(userManager.authenticate("", null));
		Assert.assertFalse(userManager.authenticate("not_found", "xx"));
	}

	@Test
	public void testAuthenticateFailureWithMissingUser() {

		Assert.assertFalse(userManager.authenticate("user_not_found", "xxx"));
	}

	@Test
	public void testSaveAndLoad() {
		String username = "user_" + UUID.randomUUID().toString();
		ObjectNode a = new ObjectMapper().createObjectNode();
		a.put("username", username);
		
		userManager.save(a);

		Optional<ObjectNode> b = userManager.getUserAsJsonObject(username);

		Assert.assertTrue(b.isPresent());

	}


}

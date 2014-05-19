package io.macgyver.core.auth;

import io.macgyver.test.MacGyverIntegrationTest;

import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.TxBlock;
import org.mapdb.TxMaker;
import org.mapdb.TxRollbackException;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

public class UserManagerTest extends MacGyverIntegrationTest {

	@Autowired
	UserManager userManager;

	@Autowired
	TxMaker txMaker;
	@Test
	public void testUserManager() {
		Preconditions.checkNotNull(userManager);
		String username = UUID.randomUUID().toString();
		Optional<JsonObject> x = userManager.getUserAsJsonObject(username);
		Assert.assertFalse(x.isPresent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveWithInvalidArg() {
		Preconditions.checkNotNull(userManager);
		JsonObject u = new JsonObject();
		userManager.save(u);
	}

	@Test
	public void testAuthenticateFailureWithNoPassword() {
		String username = "user_" + UUID.randomUUID().toString();
		JsonObject a = new JsonObject();
		a.addProperty("username", username);
		userManager.save(a);

		Assert.assertFalse(userManager.authenticate(username, "xxx"));
	}

	@Test
	public void testAuthentication() {
		String username = "user_" + UUID.randomUUID().toString();
		JsonObject a = new JsonObject();
		a.addProperty("username", username);
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
		JsonObject a = new JsonObject();
		a.addProperty("username", username);
		userManager.save(a);

		Optional<JsonObject> b = userManager.getUserAsJsonObject(username);

		Assert.assertTrue(b.isPresent());

		System.out.println(b.get());

	}

	@Test
	public void testGarbage() {
		final String username = UUID.randomUUID().toString();
		TxBlock b = new TxBlock() {

			@Override
			public void tx(DB db) throws TxRollbackException {
				
				Map<String, String> x = db.getTreeMap("users");
				x.put(username, "garbage");
			}
		};
		txMaker.execute(b);
		
		Optional<JsonObject> x = userManager.getUserAsJsonObject(username);
		Assert.assertFalse(x.isPresent());
	}
}

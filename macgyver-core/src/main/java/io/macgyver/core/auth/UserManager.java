package io.macgyver.core.auth;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.mapdb.DB;
import org.mapdb.TxBlock;
import org.mapdb.TxMaker;
import org.mapdb.TxRollbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lambdaworks.crypto.SCryptUtil;

public class UserManager {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	TxMaker txMaker;

	Gson gson = new Gson();

	public Optional<JsonObject> getUserAsJsonObject(final String id) {
		final AtomicReference<JsonObject> ref = new AtomicReference<JsonObject>();
		TxBlock b = new TxBlock() {

			@Override
			public void tx(DB db) throws TxRollbackException {
				Map<String, String> x = db.getTreeMap("users");
				String json = x.get(id);
				JsonObject obj = gson.fromJson(json, JsonObject.class);
				ref.set(obj);
			}
		};
		try {
			txMaker.execute(b);
			JsonObject x = ref.get();
			return Optional.fromNullable(x);
		} catch (Exception e) {
			logger.warn("could not load user: {}", id);
			return Optional.absent();
		}

	}

	public void save(final JsonObject obj) {
		Preconditions.checkNotNull(obj);
		Preconditions.checkArgument(obj.has("username"),
				"username must be present");
		final String username = obj.get("username").getAsString();
		if (!obj.has("roles")) {
			obj.add("roles", new JsonArray());
		}
		TxBlock b = new TxBlock() {

			@Override
			public void tx(DB db) throws TxRollbackException {
				db.getTreeMap("users").put(username, gson.toJson(obj));

			}
		};
		txMaker.execute(b);
	}

	public boolean authenticate(String username, String password) {
		try {
			Optional<JsonObject> u = getUserAsJsonObject(username);
			String hash = u.get().get("scrypt").getAsString();
			return SCryptUtil.check(password, hash);
		} catch (Exception e) {
			return false;
		}
	}

	public void setPassword(String username, String password) {
		Optional<JsonObject> u = getUserAsJsonObject(username);
		String hash = SCryptUtil.scrypt(password, 4096, 8, 1);
		JsonObject obj = u.get();
		obj.addProperty("scrypt", hash);
		save(obj);
	}
}

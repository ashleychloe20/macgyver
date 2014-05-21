package io.macgyver.core.auth;

import io.macgyver.jsondb.JsonDbTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.lambdaworks.crypto.SCryptUtil;

public class UserManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired(required=true)
	JsonDbTemplate template;



	public Optional<ObjectNode> getUserAsJsonObject(final String id) {
		
		Preconditions.checkNotNull(template);
		return template.findOne("users", id);
		

	}

	public void save(final ObjectNode obj) {
		Preconditions.checkNotNull(obj);
		Preconditions.checkArgument(obj.has("username"),
				"username must be present");
		
		final String username = obj.path("username").asText();
		
		obj.put("_id", username);
	
		if (!obj.has("roles")) {
			obj.put("roles", new ObjectMapper().createArrayNode());
		}
		
		template.save("users", obj);
	}

	public boolean authenticate(String username, String password) {
		try {
			Optional<ObjectNode> u = getUserAsJsonObject(username);
			String hash = u.get().path("scrypt").asText();
			return SCryptUtil.check(password, hash);
		} catch (Exception e) {
			return false;
		}
	}

	public void setPassword(String username, String password) {
		Optional<ObjectNode> u = getUserAsJsonObject(username);
		String hash = SCryptUtil.scrypt(password, 4096, 8, 1);
		ObjectNode obj = u.get();
		obj.put("scrypt", hash);
		save(obj);
	}
}

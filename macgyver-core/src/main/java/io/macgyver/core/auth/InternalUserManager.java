package io.macgyver.core.auth;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lambdaworks.crypto.SCryptUtil;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

public class InternalUserManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TransactionalGraph graph;

	public Optional<InternalUser> getInternalUser(final String id) {
	
			Iterator<Vertex> t = graph.query().has("vertexType", "user")
					.has("macUsername", id).vertices().iterator();
			if (t.hasNext()) {
				Vertex v = t.next();
				InternalUser u = new InternalUser();
				u.username = v.getProperty("username");
				Object rolesObj = v.getProperty("roles");
				if (rolesObj == null) {
					u.roles = Lists.newArrayList();
				} else if (rolesObj instanceof String[]) {
					u.roles = Lists.newArrayList((String[]) rolesObj);
				} else if (rolesObj instanceof List) {
					u.roles = (List) rolesObj;
				}

				return Optional.of(u);
			}

		
		return Optional.absent();
	}

	public boolean authenticate(String username, String password) {
		try {
			Vertex v = graph.getVertices("macUsername", username).iterator()
					.next();

			String hashValue = v.getProperty("scryptHash");
			return SCryptUtil.check(password, Strings.nullToEmpty(hashValue));
		} catch (NoSuchElementException e) {
			// user not found
			return false;
		} catch (IllegalArgumentException e) {
			// invalid or msising hash
			return false;
		}
	}

	public void setPassword(String username, String password) {

		String hash = SCryptUtil.scrypt(password, 4096, 8, 1);

		Vertex v = graph.getVertices("macUsername", username).iterator().next();

		v.setProperty("scryptHash", hash);

	}

	public void setRoles(String username, List<String> roles) {

		Vertex v = graph.getVertices("macUsername", username).iterator().next();
		v.setProperty("roles", roles.toArray(new String[0]));

	}

	public InternalUser createUser(String username, List<String> roles) {

		if (getInternalUser(username).isPresent()) {
			throw new IllegalArgumentException("user already exists: "
					+ username);
		}
		username = username.trim().toLowerCase();
		Vertex v = graph.addVertex(null);
		v.setProperty("macUsername", username);
		v.setProperty("roles", roles.toArray(new String[0]));
		v.setProperty("vertexType", "user");
		InternalUser u = new InternalUser();
		u.username = username;
		u.roles = Lists.newArrayList(roles);

		return u;

	}

	@PostConstruct
	public void initializeGraphDatabase() {
		try {
			// OrientGraph og = (OrientGraph) graph;
			// og.createKeyIndex("macUsername", Vertex.class, new Parameter(
			// "type", "UNIQUE"));
		} catch (Exception e) {
			logger.info(e.toString());
		}

	}
}

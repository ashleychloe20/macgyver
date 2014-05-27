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
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

public class InternalUserManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TitanGraph graph;

	public Optional<InternalUser> getInternalUser(final String id) {
		try {
			Iterator<Vertex> t = graph.query().has("vertexType", "user").has("macUsername", id).vertices().iterator();
			if  (t.hasNext()) {
				Vertex v = t.next();
				InternalUser u = new InternalUser();
				u.username = v.getProperty("username");
				String[] roles = v.getProperty("roles");
				if (roles==null) {
					u.roles = Lists.newArrayList();
				}
				else {
					u.roles = Lists.newArrayList(roles);
				}
				
				return Optional.of(u);
			}
			
		}
		finally {
			graph.commit();
		}
		return Optional.absent();
	}

	public boolean authenticate(String username, String password) {
		try {
			Vertex v = graph.getVertices("macUsername", username).iterator().next();
			
			String hashValue = v.getProperty("scryptHash");
			return SCryptUtil.check(password, Strings.nullToEmpty(hashValue));
		}
		catch(NoSuchElementException e) {
			// user not found
			return false;
		}
		catch(IllegalArgumentException e) {
			// invalid or msising hash
			return false;
		}
		finally {
			graph.commit();
		}
	}

	public void setPassword(String username, String password) {

		String hash = SCryptUtil.scrypt(password, 4096, 8, 1);
		try {
			Vertex v = graph.getVertices("macUsername", username).iterator().next();
			
			v.setProperty("scryptHash", hash);
		}
		finally {
			graph.commit();
		}
	}

	public void setRoles(String username, List<String> roles) {
		try {
			Vertex v = graph.getVertices("macUsername", username).iterator().next();
			v.setProperty("roles", roles.toArray(new String[0]));
		}
		finally {
			graph.commit();
		}
	}

	public InternalUser createUser(String username, List<String> roles) {
		try {
			username=username.trim().toLowerCase();
			Vertex v = graph.addVertex(null);
			v.setProperty("macUsername", username);
			v.setProperty("roles", roles.toArray(new String[0]));
			v.setProperty("vertexType", "user");
			InternalUser u = new InternalUser();
			u.username=username;
			u.roles = Lists.newArrayList(roles);
			
			return u;
		}
		finally {
			graph.commit();
		}
	}

	@PostConstruct
	public void initializeGraphDatabase() {
		try {
			graph.makeKey("macUsername").dataType(String.class).indexed(Vertex.class).unique().make();
		}
		catch (Exception e) {}
	}
}

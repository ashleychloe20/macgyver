package io.macgyver.core.auth;

import io.macgyver.core.MacGyverException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.neo4j.graphdb.ConstraintViolationException;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lambdaworks.crypto.SCryptUtil;

public class InternalUserManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GraphDatabaseService graphDb;

	public Optional<InternalUser> getInternalUser(final String id) {

		Label userLabel = DynamicLabel.label("User");

		try (Transaction tx = graphDb.beginTx()) {

			try (ResourceIterator<Node> users = graphDb
					.findNodesByLabelAndProperty(userLabel, "username", id)
					.iterator()) {

				if (users.hasNext()) {
					Node n = users.next();
					String username = (String) n.getProperty("username");
					String roles[] = (String[]) n.getProperty("roles",
							new String[0]);

					InternalUser u = new InternalUser();
					u.username = username;
					u.roles = Lists.newArrayList(roles);
					u.scryptHash = (String) n.getProperty("scryptHash", null);

					return Optional.of(u);
				} else {
					logger.info("user not found: {}", id);
					return Optional.absent();
				}

			}
		}

	}

	public boolean authenticate(String username, String password) {
		try {
			Optional<InternalUser> u = getInternalUser(username);

			if (!u.isPresent()) {
				return false;
			}
			return SCryptUtil.check(password,
					Strings.nullToEmpty(u.get().scryptHash));
		} catch (Exception e) {
			logger.info("problem authenticating: {}", e.toString());
		}
		return false;
	}

	public void setPassword(String username, String password) {

		String hash = SCryptUtil.scrypt(password, 4096, 8, 1);

		Label userLabel = DynamicLabel.label("User");

		try (Transaction tx = graphDb.beginTx()) {

			try (ResourceIterator<Node> users = graphDb
					.findNodesByLabelAndProperty(userLabel, "username",
							username).iterator()) {

				if (users.hasNext()) {
					Node n = users.next();

					n.setProperty("scryptHash", hash);
					tx.success();
				} else {
					logger.info("user not found: {}", username);

				}

			}
		}
	}
	public void setRoles(String username, List<String> roles) {
		Preconditions.checkNotNull(username);
		
	
		Label userLabel = DynamicLabel.label("User");

		try (Transaction tx = graphDb.beginTx()) {

			try (ResourceIterator<Node> users = graphDb
					.findNodesByLabelAndProperty(userLabel, "username",
							username).iterator()) {

				if (users.hasNext()) {
					Node n = users.next();

					n.setProperty("roles", roles.toArray(new String[0]));
					tx.success();
				} else {
					logger.info("user not found: {}", username);

				}

			}
		}
	}
	public InternalUser createUser(String username, List<String> roles) {
		Label userLabel = DynamicLabel.label("User");

		try (Transaction tx = graphDb.beginTx()) {

			Node n = graphDb.createNode(userLabel);
			n.setProperty("username", username.toLowerCase().trim());
			n.setProperty("roles", roles.toArray(new String[0]));
			tx.success();
		}

		Optional<InternalUser> u = getInternalUser(username);
		if (u.isPresent()) {
			return u.get();
		}

		throw new MacGyverException("could not create user: " + username);

	}

	@PostConstruct
	public void initializeGraphDatabase() {
		try (Transaction tx = graphDb.beginTx()) {
			Schema schema = graphDb.schema();
			ConstraintDefinition def = schema
					.constraintFor(DynamicLabel.label("User"))
					.assertPropertyIsUnique("username").create();
			tx.success();
		} catch (ConstraintViolationException e) {
			logger.info("unique constraint User.username already established");
		}
	}
}

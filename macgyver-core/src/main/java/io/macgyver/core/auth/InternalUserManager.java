/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.core.auth;

import io.macgyver.neo4j.rest.Neo4jRestClient;
import io.macgyver.neo4j.rest.Result;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lambdaworks.crypto.SCryptUtil;

public class InternalUserManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	Neo4jRestClient neo4j;

	public Optional<InternalUser> getInternalUser(final String id) {

		String q = "match (u:User) where u.username={username} return u.username,u.roles";

		Result r = neo4j.execCypher(q, "username", id.toLowerCase());
		if (r.next()) {
			InternalUser u = new InternalUser();
			u.username = r.getString("u.username");
			
			u.roles = (List<String>) r.getList("u.roles");
			
			
	
			return Optional.of(u);
		}

		return Optional.absent();
	}

	public boolean authenticate(String username, String password) {
		try {
			String q = "match (u:User) where u.username={username} return u.scryptHash";
			ObjectNode n = new ObjectMapper().createObjectNode();
			n.put("username", username);
			Result r = neo4j.execCypher(q, n);
			if (r.next()) {

				String hashValue = Strings.emptyToNull(r
						.getString("u.scryptHash"));
				if (hashValue == null) {
					return false;
				}
				return SCryptUtil.check(password,
						Strings.nullToEmpty(hashValue));

			} else {
				return false;
			}
		} catch (Exception e) {
			logger.warn("auth error", e);
			return false;
		}

	}

	public void setPassword(String username, String password) {

		String hash = SCryptUtil.scrypt(password, 4096, 8, 1);

		String c = "match (u:User) where u.username={username} set u.scryptHash={hash}";
	

		neo4j.execCypher(c, "username",username,"hash",hash);

	}

	public void setRoles(String username, List<String> roles) {
		
		String c = "match (u:User) where u.username={username} set u.roles={roles}";
		neo4j.execCypher(c,"username",username,"roles",roles);
	

	}

	public InternalUser createUser(String username, List<String> roles) {

		if (getInternalUser(username).isPresent()) {
			throw new IllegalArgumentException("user already exists: "
					+ username);
		}
		username = username.trim().toLowerCase();

		String cypher = "create (u:User {username:{username}})";
		neo4j.execCypher(cypher, "username", username);

		setRoles(username,roles);
		InternalUser u = new InternalUser();
		u.username = username;
		u.roles = Lists.newArrayList();

		return u;

	}

	@PostConstruct
	public void initializeGraphDatabase() {
		try {
			
			String cipher = "CREATE CONSTRAINT ON (u:User) ASSERT u.username IS UNIQUE";		
			neo4j.execCypher(cipher);
			
		} catch (Exception e) {
			logger.warn(e.toString());
		}
		
		
		Optional<InternalUser> admin = getInternalUser("admin");
		if (admin.isPresent()) {
			logger.debug("admin user already exists");
		}
		else {
			logger.info("adding admin user");
			List<String> roleList = Lists.newArrayList("ROLE_MACGYVER_SHELL","ROLE_MACGYVER_UI", "ROLE_MACGYVER_ADMIN","ROLE_MACGYVER_USER");
			
			createUser("admin", roleList);
			setPassword("admin", "admin");
			
		}

	}
}

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

import io.macgyver.test.MacGyverIntegrationTest;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class UserManagerTest extends MacGyverIntegrationTest {

	@Autowired
	InternalUserManager userManager;

	@Test
	public void testAutowire() {
		Assert.assertNotNull(userManager);

	}

	@Test
	public void testAuthentication() {

		Assert.assertFalse(userManager.authenticate("admin", "xxx"));

	}

	@Test
	public void testAuthenticateFailureWithMissingUser() {

		Assert.assertFalse(userManager.authenticate("user_not_found", "xxx"));
	}

	@Test
	public void testUpdateRoles() {
		String username = "user_" + UUID.randomUUID().toString();
		userManager.createUser(username, Lists.newArrayList("ROLE_A","ROLE_X"));
		
		Assert.assertTrue(userManager.getInternalUser(username).get().getRoles().contains("ROLE_A"));
		Assert.assertFalse(userManager.getInternalUser(username).get().getRoles().contains("ROLE_B"));
		userManager.setRoles(username, Lists.newArrayList("ROLE_A","ROLE_B"));
		
		Assert.assertTrue(userManager.getInternalUser(username).get().getRoles().contains("ROLE_A"));
		Assert.assertTrue(userManager.getInternalUser(username).get().getRoles().contains("ROLE_B"));
	}
	
	
	@Test
	public void testCreateUser() {
		String username = "user_" + UUID.randomUUID().toString();

		InternalUser u= userManager.createUser(username, Lists.newArrayList("MAC"));
		
		InternalUser u2 = userManager.getInternalUser(username).get();
		//userManager.createUser(username, Lists.newArrayList("MAC"));
	}
	@Test
	public void testSaveAndLoad() {
		String username = "user_" + UUID.randomUUID().toString();

		userManager.createUser(username, Lists.newArrayList("MAC"));
		Assert.assertFalse(userManager.authenticate(username, "abc123"));
		userManager.setPassword(username, "abc123");
		Assert.assertTrue(userManager.authenticate(username, "abc123"));

	}

	@Test(expected=IllegalArgumentException.class)
	public void testUnique() {
		String username = "bob";
		List<String> roles = Lists.newArrayList();

		userManager.createUser(username, roles);

		Assert.assertTrue(userManager.getInternalUser("bob").isPresent());

		userManager.createUser(username, roles);

	}

}

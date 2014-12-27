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
package io.macgyver.plugin.newrelic;

import java.io.IOException;

import io.macgyver.core.ServiceInvocationException;
import io.macgyver.test.MacGyverIntegrationTest;


import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public class NewRelicClientTest extends MacGyverIntegrationTest {

	String apiKey;

	@org.junit.Before
	public void checkIfKeyIsAvailable() {
		apiKey = getPrivateProperty("newrelic.apiKey");
	}

	@Test
	public void testX() throws IOException {
		Assume.assumeFalse(Strings.isNullOrEmpty(apiKey));
		NewRelicClient c = new NewRelicClient();
		c.setApiKey(apiKey);

		JsonNode x = c.get("applications");
		logger.info("response: "+x);
		Assert.assertTrue(x.has("applications"));

	}

	@Test
	public void testInvalidKey() {

		try {
			NewRelicClient c = new NewRelicClient();
			c.setApiKey("invalid");

			JsonNode x = c.get("applications.json");

			Assert.fail("expected exception");
		} catch (Exception e) {
			Assertions.assertThat(e).isInstanceOf(
					ServiceInvocationException.class).hasMessageContaining("Invalid API Key");
		}
	}

}

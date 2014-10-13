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
package io.macgyver.chat.hipchat;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.test.MacGyverIntegrationTest;

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ning.http.client.AsyncHttpClient;

public class HipChatClientTest extends MacGyverIntegrationTest {

	@Autowired
	@Qualifier("macAsyncHttpClient")
	AsyncHttpClient client;

	@Autowired
	ServiceRegistry factory;
	
	
	@Test
	public void testIt() throws Exception {

		
		Assert.assertNotNull(factory.get("hipchatTest"));
		
		
		HipChat client = new HipChat(this.client);
		client.setApiKey("");
		client.sendMessage("test from "
				+ InetAddress.getLocalHost().getHostName(),
				"Notification Testing", "12345678901234", true, null);

		//Thread.sleep(5000);

	}
	
	
}

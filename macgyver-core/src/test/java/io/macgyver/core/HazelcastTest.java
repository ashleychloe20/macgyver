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
package io.macgyver.core;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.HazelcastInstance;

import io.macgyver.test.MacGyverIntegrationTest;

public class HazelcastTest extends MacGyverIntegrationTest {

	@Autowired
	HazelcastInstance hazelcast;
	
	@Test
	public void testHazelcast() {
		assertNotNull(hazelcast);
		
		Map<Object,Object> m1 = hazelcast.getMap("test");
		Map<Object,Object> m2 = hazelcast.getMap("test");
		
		m1.put("abc", "123");
		Assert.assertSame(m1,m2);
		Assert.assertEquals("123",m2.get("abc"));
	}
}

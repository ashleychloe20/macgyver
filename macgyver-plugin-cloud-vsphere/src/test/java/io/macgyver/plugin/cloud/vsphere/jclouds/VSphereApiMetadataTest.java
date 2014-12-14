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
package io.macgyver.plugin.cloud.vsphere.jclouds;

import static org.junit.Assert.assertEquals;
import io.macgyver.plugin.cloud.vsphere.jclouds.VSphereApiMetadata;

import org.junit.Assert;
import org.junit.Test;

public class VSphereApiMetadataTest {

	
	@Test
	public void testIt() {
		VSphereApiMetadata md = new VSphereApiMetadata().toBuilder().defaultEndpoint("https://ssx").build();
		
		assertEquals("https://ssx",md.getDefaultEndpoint().get());
	}
	
	@Test
	public void testX() {
		VSphereApiMetadata md = new VSphereApiMetadata().toBuilder().credentialName("hello").build();
		
		assertEquals("hello", md.getCredentialName().get());
	}
}

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
package io.macgyver.xson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.macgyver.xson.impl.PathPredicateImpl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.CharStreams;

public class PathPredicateTest {

	@Test
	public void testX() throws IOException {
		
		InputStream is = getClass().getClassLoader().getResourceAsStream("json/sample.json");
		Assert.assertNotNull("must be able to load sample data",is);
		String s = CharStreams.toString(new InputStreamReader(is));
		
		
		ObjectNode n = Xson.convert(s, ObjectNode.class);
		
		// $..phoneNumbers[?(@.type=="iPhone" ||@.type=="home")]
	
		
		Assert.assertTrue(Xson.pathPredicate("$..batters.batter[?(@.type=='Chocolate')]").apply(n));
		
		Assert.assertFalse(Xson.pathPredicate("$..batter[?(@.type=='ChocolateX')]").apply(n));
		
		
	
	}
}

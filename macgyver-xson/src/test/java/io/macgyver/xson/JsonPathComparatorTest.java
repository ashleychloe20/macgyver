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

import io.macgyver.xson.Xson.SortOrder;
import io.macgyver.xson.impl.JsonPathComparatorImpl;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class JsonPathComparatorTest {

	ObjectMapper m = new ObjectMapper();
	List<ObjectNode> movieList;
	
	@Before
	public void testList() {
		List<ObjectNode> list = Lists.newArrayList();
		
		ObjectNode b = m.createObjectNode();
		
		
		b.put("year", 2013);
		b.put("name", "Nebraska");
		b.put("id", 22);
		list.add(b);
		
		b = m.createObjectNode();
		b.put("year", 1998);
		b.put("name", "Big Lebowski, The");	
		b.put("id", 120);
		list.add(b);
		
		b = m.createObjectNode();
		b.put("year",1994);
		b.put("name", "Pulp Fiction");
		b.put("id", 1);
		list.add(b);
		
		movieList = list;
	}
	
	@Test
	public void testXX() {
		List<ObjectNode> filteredList = ImmutableList.copyOf(Iterables.filter(movieList, Xson.pathPredicate("$.[?(@name==\"Pulp Fiction\")]")));

	}
	
	@Test
	public void testStringSort2() {
		
		Collections.sort(movieList,Xson.pathComparator("$.name"));

		org.junit.Assert.assertEquals("Big Lebowski, The", movieList.get(0).get("name").asText());
		org.junit.Assert.assertEquals("Nebraska", movieList.get(1).get("name").asText());
		org.junit.Assert.assertEquals("Pulp Fiction", movieList.get(2).get("name").asText());
	}
	
	@Test
	public void testStringSortReverse() {
		
		Collections.sort(movieList,Xson.pathComparator("$.name",SortOrder.DESCENDING));
	
		org.junit.Assert.assertEquals("Big Lebowski, The", movieList.get(2).get("name").asText());
		org.junit.Assert.assertEquals("Nebraska", movieList.get(1).get("name").asText());
		org.junit.Assert.assertEquals("Pulp Fiction", movieList.get(0).get("name").asText());
	}
	@Test
	public void testNumberSort() {
		
		Collections.sort(movieList,Xson.pathComparator("$.id"));
		
		org.junit.Assert.assertEquals(1,movieList.get(0).path("id").asInt());
		org.junit.Assert.assertEquals(22,movieList.get(1).path("id").asInt());
		org.junit.Assert.assertEquals(120,movieList.get(2).path("id").asInt());
		
	
	}
	
	
	
}

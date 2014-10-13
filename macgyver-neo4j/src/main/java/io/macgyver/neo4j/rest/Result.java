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
package io.macgyver.neo4j.rest;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Result {

	boolean hasNext();
	boolean next();
	int getColumnIndex(String name);
	List<String> getColumnNames();
	
	String getString(int column);
	String getString(String column);
	List getList(String column);
	List getList(int column);
	ObjectNode getObjectNode(String column);
	ObjectNode getObjectNode(int column);
	
	
	List<ObjectNode> asVertexList(String column);
	List<ObjectNode> asVertexList(int column);

	List<ObjectNode> asVertexDataList(String column);
	List<ObjectNode> asVertexDataList(int c);
}

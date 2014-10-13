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
package io.macgyver.core.web.vaadin;

import java.io.IOException;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MenuManagerTest {

	ObjectMapper mapper = new ObjectMapper();
	
	String json = "{\n" + 
			"    \"menu\": {\n" + 
			"    \"items\" : [\n" + 
			"        {\"display\":\"Admin\",\n" + 
			"            \"items\" : [ \n" + 
			"                {\"display\" : \"Beans\",\n" + 
			"                    \"view\": \"admin/beans\"},\n" + 
			"                {\"display\" : \"Encrypt String\", \"view\": \"admin/encrypt\"}\n" + 
			"                {\"display\" : \"Scripts\", \"view\" : \"admin/scripts\" }\n" + 
			"            \n" + 
			"            ]\n" + 
			"        },\n" + 
			"        {\"display\":\"Inventory\",\n" + 
			"            \"items\": [\n" + 
			"                {\"display\" : \"App Instances\", \"view\":\"cmdb/appInstances\"}\n" + 
			"            ]\n" + 
			"        }\n" + 
			"    ]\n" + 
			"    }\n" + 
			"    \n" + 
			"}";

}

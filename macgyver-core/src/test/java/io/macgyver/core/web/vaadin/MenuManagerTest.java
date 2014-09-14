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

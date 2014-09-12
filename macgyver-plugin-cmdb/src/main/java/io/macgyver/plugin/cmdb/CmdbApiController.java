package io.macgyver.plugin.cmdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.neo4j.rest.Neo4jRestClient;
import io.macgyver.xson.Xson;

import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/api/cmdb")
public class CmdbApiController {

	Logger logger = LoggerFactory.getLogger(CmdbApiController.class);

	@Autowired
	AppInstanceManager manager;

	@Autowired
	ServiceRegistry registry;

	@Autowired
	AppInstanceManager appInstanceManager;


	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	Neo4jRestClient neo4j;
	
	

	@RequestMapping(value = "checkIn", method = { RequestMethod.PUT,
			RequestMethod.POST,RequestMethod.GET }, produces = "application/json")
	@PreAuthorize("permitAll")
	public ResponseEntity<ObjectNode>  checkIn(
			
			
			HttpServletRequest request) throws IOException {
		CheckInProcessor processor = appInstanceManager.getCheckInProcessor();

				if (!processor.checkAuth(request)) {
					return new ResponseEntity<ObjectNode>(HttpStatus.UNAUTHORIZED);
				}
				ObjectNode json = processor.process(request);
			
				processor.decorate(json);

			appInstanceManager.processCheckIn(json);

		
		return new ResponseEntity<ObjectNode>(mapper.createObjectNode(), HttpStatus.OK);
	}

	@RequestMapping(value="/appInstances", method=RequestMethod.GET) 
	@PreAuthorize("hasAnyRole('ROLE_MACGYVER_USER','ROLE_MACGYVER_API_RO')")
	public ResponseEntity<List<ObjectNode>> allAppInstances() {
		String cypher = "match (x:AppInstance) return x";
		List<ObjectNode> results = neo4j.execCypher(cypher).asVertexDataList("x");	
		beautifyTimestamps(results);
		return new ResponseEntity<List<ObjectNode>>(results, HttpStatus.OK);
	}
	
	@RequestMapping(value="/appInstances/environment/{env}", method=RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_MACGYVER_USER','ROLE_MACGYVER_API_RO')")
	public ResponseEntity<List<ObjectNode>> appInstancesByEnv(@PathVariable String env) {
		String cypher = "match (x:AppInstance {environment:{env}}) return x";
		List<ObjectNode> results = neo4j.execCypher(cypher,"env",env).asVertexDataList("x");
		beautifyTimestamps(results);
		return new ResponseEntity<List<ObjectNode>>(results, HttpStatus.OK);
	}
	
	@RequestMapping(value="/appInstances/environment/{env}/appId/{appId}", method=RequestMethod.GET) 
	@PreAuthorize("hasAnyRole('ROLE_MACGYVER_USER','ROLE_MACGYVER_API_RO')")
	public ResponseEntity<List<ObjectNode>> appInstance(@PathVariable String env, @PathVariable String appId) {
		String cypher = "match (x:AppInstance {appId:{appIds},environment:{env}}) return x";
		List<ObjectNode> results = neo4j.execCypher(cypher,"appIds",appId,"env",env).asVertexDataList("x");	
		beautifyTimestamps(results);
		return new ResponseEntity<List<ObjectNode>>(results, HttpStatus.OK);
	}
	
	protected void beautifyTimestamps(List<ObjectNode> list) {
		PrettyTime pt = new PrettyTime();
		for (ObjectNode n : list) {
			long val = n.path("lastContactTs").asLong(0);
			Date d = new Date(val);
			n.put("lastContactPrettyTs", pt.format(d));
		}
	}

}

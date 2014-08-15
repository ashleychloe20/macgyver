package io.macgyver.plugin.cmdb;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.xson.Xson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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


}

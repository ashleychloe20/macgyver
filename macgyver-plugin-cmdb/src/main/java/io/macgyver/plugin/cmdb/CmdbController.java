package io.macgyver.plugin.cmdb;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.core.util.ObjectNodeDecorator;
import io.macgyver.core.web.navigation.Menu;
import io.macgyver.core.web.navigation.MenuDecorator;
import io.macgyver.core.web.w2ui.GridDataControl;
import io.macgyver.core.web.w2ui.GridDataRequest;
import io.macgyver.neo4j.rest.Neo4jRestClient;
import io.macgyver.neo4j.rest.Result;
import io.macgyver.xson.JsonPathComparator;
import io.macgyver.xson.Xson;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.sun.corba.se.impl.ior.NewObjectKeyTemplateBase;

@Controller
@PreAuthorize("hasAnyRole('ROLE_MACGYVER_USER','ROLE_MACGYVER_ADMIN')")
public class CmdbController implements MenuDecorator {

	Logger logger = LoggerFactory.getLogger(CmdbController.class);
	@Autowired
	ServiceRegistry registry;

	@Autowired
	AppInstanceManager appInstanceManager;

	@Autowired
	Neo4jRestClient neo4j;
	
	ObjectNodeDecorator appInstanceViewDecorator = new ObjectNodeDecorator();
	
	@RequestMapping(value = "/cmdb/computeInstances")
	public ModelAndView viewComputeInstances(ModelAndView m) {

		
		JsonPathComparator pc = Xson.pathComparator("$.name")

		.build();
		
		Result r = neo4j.execCypher("match (ci:ComputeInstance) return ci");
		
		List<ObjectNode> results = r.asVertexDataList("ci");
	

		m.setViewName("cmdb/computeInstances");
		m.addObject("results", results);

		return m;

	}

	@RequestMapping(value = "/cmdb/services")
	public ModelAndView serviceInstances(ModelAndView m) {
		m.setViewName("/cmdb/services");

		return m;
	}

	@RequestMapping(value = "/cmdb/elbInstances/{id}")
	public ModelAndView elbInstanceView(ModelAndView m,
			@PathVariable("id") String id) {
		List<ObjectNode> results = Lists.newArrayList();

		m.setViewName("/cmdb/elbInstance");

	
	
		return m;
	}

	@RequestMapping(value = "/cmdb/elbInstances")
	public ModelAndView elbInstances(ModelAndView m) {
		List<ObjectNode> results = Lists.newArrayList();

		m.setViewName("/cmdb/elbInstances");

	
		m.addObject("results", results);
		return m;
	}

	
	@RequestMapping(value = "/cmdb/appInstances/data.json", produces=MediaType.APPLICATION_JSON,method={RequestMethod.POST})
	@ResponseBody
	public ObjectNode viewAppInstances(HttpServletRequest request) {

		
		
		
			String cypher = "match (ai:AppInstance) return ai";
			
			List<ObjectNode> results = neo4j.execCypher(cypher).asVertexList("ai");

			GridDataControl control = new GridDataControl(request);
			control.setDefaultSort("environment");
			
			int index=0;
			for (ObjectNode ai : results) {
				index++;
				ObjectNode n = (ObjectNode) ai.path("data");
			
				
				long ts = n.path("lastContactTs").asLong(0);
				n.put("lastContactTsPretty", ts == 0 ? "" : new PrettyTime().format(new Date(ts)));
				
				
				control.addRow(n);
			}
			
		
			
			return control.process();
	
		
		
		

	}
	
	@RequestMapping(value = "/cmdb/appInstances")
	public ModelAndView viewAppInstances(ModelAndView m) {

	
			
			JsonPathComparator pc = Xson.pathComparator("$.data.environment")
					.sortBy("$.data.host").sortBy("$.data.appId").build();
			
			String cypher = "match (ai:AppInstance) return ai";
			
			List<ObjectNode> results = neo4j.execCypher(cypher).asVertexList("ai");

			Collections.sort(results,pc);
			for (ObjectNode ai : results) {
				try {
					if (appInstanceViewDecorator!=null) {
						appInstanceViewDecorator.decorate(ai);
					}

				} catch (Exception e) {
					logger.warn("problem enriching", e);
				}
			}

			m.addObject("results", results);

			m.setViewName("/cmdb/appInstances");
			return m;
		

	}

	
	@Override
	public void decorate(Menu root) {

		root.setDisplayName("cmdb", "Inventory");

	//	root.addMenuItem("cmdb", "/cmdb/computeInstances", "Compute Instances");

		root.addMenuItem("cmdb", "/cmdb/appInstances", "App Instances");

		// root.addMenuItem("cmdb","/cmdb/services","Services");

	//	root.addMenuItem("cmdb", "/cmdb/elbInstances", "ELB Instances");
	}

	public ObjectNodeDecorator getAppInstanceViewDecorator() {
		return appInstanceViewDecorator;
	}

	public void setAppInstanceViewDecorator(
			ObjectNodeDecorator appInstanceViewDecorator) {
		this.appInstanceViewDecorator = appInstanceViewDecorator;
	}

}

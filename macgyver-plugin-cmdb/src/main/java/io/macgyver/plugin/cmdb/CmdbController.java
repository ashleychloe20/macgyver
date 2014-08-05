package io.macgyver.plugin.cmdb;

import io.macgyver.core.service.ServiceRegistry;
import io.macgyver.core.web.navigation.Menu;
import io.macgyver.core.web.navigation.MenuDecorator;
import io.macgyver.neo4j.rest.Neo4jRestClient;
import io.macgyver.neo4j.rest.Result;
import io.macgyver.xson.JsonPathComparator;
import io.macgyver.xson.Xson;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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
	

	@RequestMapping(value = "/cmdb/computeInstances")
	public ModelAndView viewComputeInstances(ModelAndView m) {

		List<ObjectNode> results = Lists.newArrayList();

		JsonPathComparator pc = Xson.pathComparator("$.name")

		.build();
		
		Result r = neo4j.execCypher("match (ci:ComputeInstance) return ci");
		
		Iterable<ObjectNode> t = r.asObjectNodeIterable("ci");
		

		Iterables.addAll(results, t);

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

	@RequestMapping(value = "/cmdb/appInstances")
	public ModelAndView viewAppInstances(ModelAndView m) {

	
			List<ObjectNode> results = Lists.newArrayList();

			JsonPathComparator pc = Xson.pathComparator("$.profile")
					.sortBy("$.host").sortBy("$.artifactId").build();
			
			String cypher = "match (ai:AppInstance) return ai";
			
			Iterable<ObjectNode> t = neo4j.execCypher(cypher).asObjectNodeIterable("ai");

			Iterables.addAll(results, t);

			for (ObjectNode ai : results) {
				try {
				

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

		root.setDisplayName("cmdb", "CMDB");

	//	root.addMenuItem("cmdb", "/cmdb/computeInstances", "Compute Instances");

		root.addMenuItem("cmdb", "/cmdb/appInstances", "App Instances");

		// root.addMenuItem("cmdb","/cmdb/services","Services");

	//	root.addMenuItem("cmdb", "/cmdb/elbInstances", "ELB Instances");
	}

}

package io.macgyver.core.web;

import io.macgyver.core.crypto.Crypto;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceProvider;
import io.macgyver.core.resource.provider.filesystem.FileSystemResourceProvider;
import io.macgyver.core.scheduler.AutoScheduler;
import io.macgyver.core.script.ExtensionResourceProvider;

import java.io.IOException;
import java.nio.file.spi.FileSystemProvider;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.neo4j.helpers.collection.Iterables;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component("macAdminController")
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_MACGYVER_ADMIN')")
public class AdminController {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	Crypto crypto;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ExtensionResourceProvider extensionProvider;

	@Autowired
	AutoScheduler autoScheduler;
	
	@RequestMapping(value = "/encryptString", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView encryptString() {
		ModelAndView m = new ModelAndView("admin/encryptString");
		return m;
	}

	@RequestMapping(value = "/script", method = { RequestMethod.GET })
	public ModelAndView script(
			@RequestParam(value = "editor", required = false) String script,
			@ModelAttribute ModelAndView m) throws GeneralSecurityException {

		m.setViewName("admin/script");

		return m;
	}

	@RequestMapping(value = "/scriptEval", method = { RequestMethod.POST })
	@ResponseBody
	public String scriptEval(
			@RequestParam(value = "editor", required = false) String script,
			@ModelAttribute ModelAndView m) throws GeneralSecurityException {

		logger.info("script: {}", script);

		return "test";
	}

	@RequestMapping(value = "/encryptString", method = RequestMethod.POST)
	public ModelAndView encryptString(
			@RequestParam(value = "input") String input,
			@RequestParam(value = "key") String key,
			@ModelAttribute ModelAndView m) throws GeneralSecurityException {
		m.setViewName("admin/encryptString");
		m.addObject("cipherText", crypto.encryptString(input, key));
		return m;
	}

	/*
	 * public ModelAndView home() { ModelAndView m = new
	 * ModelAndView("home.rythm"); return m;
	 * 
	 * }
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/beans")
	public ModelAndView viewBeans(ModelAndView m) {
		List list = com.google.common.collect.Lists.newArrayList();
		String[] names = applicationContext.getBeanDefinitionNames();
		Arrays.sort(names);
		for (String name : names) {
			Map<String, Object> h = Maps.newHashMap();
			h.put("name", name);

			try {
				Object x = applicationContext.getBean(name);

				h.put("className", x.getClass().getName());
			} catch (Exception e) {
			}
			list.add(h);

		}

		m.addObject("beans", list);

		m.setViewName("admin/beans");

		return m;
	}

	@RequestMapping(value = "/refreshResourceProvider")
	public ModelAndView refreshResourceProvider(ModelAndView m)
			throws IOException {

		logger.info("refresh!!!");
		extensionProvider.refresh();

		m.setViewName("admin/refreshResourceProvider");
		return m;
	}

	Optional<Resource> findResourceByHash(String hash) throws IOException {
		for (Resource r : extensionProvider.findResources()) {
			String resourceHash = r.getHash();
			if (resourceHash.equals(hash)) {
				return Optional.of(r);
			}
		}
		return Optional.absent();
	}

	@RequestMapping(value = "/scripts/exec")
	public ModelAndView execScript(ModelAndView m, HttpServletRequest request)
			throws IOException, SchedulerException {

		String hash = request.getParameter("scriptHash");
		

		Optional<Resource> r = findResourceByHash(hash);

		if (r.isPresent()) {
			autoScheduler.scheduleImmediate(r.get());
		}

		m.setViewName("/admin/scripts/viewResult");
		return m;
	}

	@RequestMapping(value = "/scripts")
	public ModelAndView listResources(ModelAndView m) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<ObjectNode> list = Lists.newArrayList();

		for (Resource r : extensionProvider.findResources()) {
			ResourceProvider rp = r.getResourceProvider();

			if (r.getPath().startsWith("scripts/")) {
				ObjectNode n = mapper.createObjectNode();
				n.put("path", r.getPath());
				if (rp.getClass().equals(FileSystemResourceProvider.class)) {
					n.put("providerType", "filesystem");
				} else if (rp.getClass().getName().contains("Git")) {
					n.put("providerType", "git");
				}
				n.put("hash", r.getHash());
				list.add(n);
			}

		}

		m.setViewName("/admin/scripts/viewScripts");
		m.addObject("scripts", list);
		return m;
	}

}
package io.macgyver.core.web;

import io.macgyver.core.crypto.Crypto;
import io.macgyver.core.script.ExtensionResourceProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
	public ModelAndView refreshResourceProvider( ModelAndView m) throws IOException {

		logger.info("refresh!!!");
		extensionProvider.refresh();
		
		m.setViewName("admin/refreshResourceProvider");
		return m;
	}

}
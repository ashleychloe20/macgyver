package io.macgyver.core.web;

import io.macgyver.core.crypto.Crypto;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;

@Component
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	Crypto crypto;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@RequestMapping(value="/encryptString",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView encryptString() {
		ModelAndView m = new ModelAndView("admin.encryptString.rythm");
		return m;
	}
	
	@RequestMapping(value="/script",method={RequestMethod.GET})
	public ModelAndView script(
			@RequestParam(value = "editor",required=false) String script,
			@ModelAttribute ModelAndView m) throws GeneralSecurityException {
		
		System.out.println("script: "+script);
		m.setViewName("admin/script.rythm");
	
		return m;
	}	
	
	@RequestMapping(value="/scriptEval",method={RequestMethod.POST})
	@ResponseBody
	public String scriptEval(
			@RequestParam(value = "editor",required=false) String script,
			@ModelAttribute ModelAndView m) throws GeneralSecurityException {
		
		System.out.println("script: "+script);
	
	
		return "test";
	}		
	
	@RequestMapping(value = "/encryptString", method = RequestMethod.POST)
	public ModelAndView encryptString(
			@RequestParam(value = "input") String input,
			@RequestParam(value = "key") String key,
			@ModelAttribute ModelAndView m) throws GeneralSecurityException {
		m.setViewName("admin/encryptString.rythm");
		m.addObject("cipherText", crypto.encryptString(input, key));
		return m;
	}
	/*
	public ModelAndView home() {
		ModelAndView m = new ModelAndView("home.rythm");
		return m;
		
	}
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/beans")
	public ModelAndView viewBeans( ModelAndView m) {
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

		m.setViewName("/admin/beans.rythm");

		return m;
	}
	
}
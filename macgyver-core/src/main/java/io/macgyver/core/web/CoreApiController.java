package io.macgyver.core.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import io.macgyver.core.CoreSystemInfo;
import io.macgyver.core.script.ExtensionResourceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.google.gson.Gson;

@RequestMapping("/api/core")
@Controller("macCoreApiController")
public class CoreApiController {

	Logger logger = LoggerFactory.getLogger(CoreApiController.class);
	
	@Autowired
	CoreSystemInfo coreRevisionInfo;

	
	@Autowired
	ExtensionResourceProvider extensionProvider;
	
	@RequestMapping(value = "/systemInfo", method=RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@PreAuthorize("permitAll")
	public String coreRevisionInfo() {

		return new Gson().toJson(coreRevisionInfo.getData());

	}
	
	@RequestMapping(value="refreshResourceProviderHook",produces="application/json")
	@ResponseBody
	@PreAuthorize("permitAll")
	public String refreshResourceProviderHook(HttpServletRequest rquest) throws IOException {
		logger.info(rquest.getParameter("payload"));
		extensionProvider.refresh();
		return "{}";
	}
	
	
	@RequestMapping(value = "/error")
	@ResponseBody
	public String errorHandler() {

		return "problem";

	}
}

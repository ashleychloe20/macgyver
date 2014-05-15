package io.macgyver.core.web;

import java.util.Map;

import io.macgyver.core.CoreSystemInfo;

import javax.ws.rs.ApplicationPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;
import com.google.gson.Gson;

@RequestMapping("/api/core")
@Controller
public class CoreApiController {

	@Autowired
	CoreSystemInfo coreRevisionInfo;

	@RequestMapping(value = "/systemInfo", produces = { "application/json" })
	@ResponseBody
	public String coreRevisionInfo() {

		return new Gson().toJson(coreRevisionInfo.getData());

	}
}

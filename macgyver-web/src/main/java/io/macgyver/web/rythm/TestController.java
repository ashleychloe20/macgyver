package io.macgyver.web.rythm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Controller
public class TestController {
	

	@RequestMapping(value="/mvctest/a", method=RequestMethod.GET)
	public String doit(ModelMap m) {
		WebMvcConfigurationSupport s;
		

		return "test.rythm";
	}

}

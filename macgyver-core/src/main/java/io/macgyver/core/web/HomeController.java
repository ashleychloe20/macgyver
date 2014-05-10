package io.macgyver.core.web;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Component
@Controller
public class HomeController {

	@RequestMapping("/")
	@ResponseBody
	public ModelAndView home() {
		ModelAndView m = new ModelAndView("home.rythm");
		return m;
		
	}
}

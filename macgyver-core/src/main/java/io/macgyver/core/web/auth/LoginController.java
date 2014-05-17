package io.macgyver.core.web.auth;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Component(value="macLoginController")
@Controller
public class LoginController {

	
	
		@RequestMapping("/login")
		@ResponseBody
		public ModelAndView home() {
			
		
			ModelAndView m = new ModelAndView("auth/login.rythm");
			return m;
			
		}
	
	
}

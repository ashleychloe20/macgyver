package io.macgyver.http.spark;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Map;

import io.macgyver.http.spark.freemarker.MacGyverFreeMarkerRoute;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import spark.Request;
import spark.Response;
import spark.Route;

public class AuthSparklet extends Sparklet {
	Logger logger = LoggerFactory.getLogger(AuthSparklet.class);

	public void init() {

		before(new spark.Filter() {
			
			@Override
			public void handle(Request request, Response response) {
				Object p = SecurityUtils.getSubject().getPrincipal();
				if (p==null) {
					//response.redirect("/auth/login");
				}
			
				
			}
		});
		
		get(new Route("/auth/logout") {
			@Override
			public Object handle(Request request, Response response) {
				SecurityUtils.getSubject().logout();
				response.redirect("/auth/login");
				halt();
				return null;
			}
		});

		get(new MacGyverFreeMarkerRoute("/auth/login") {

			@Override
			public Object handle(Request request, Response response) {
				logger.debug("subject: {}", SecurityUtils.getSubject()
						.getPrincipal());

			
				Map<String, String> m = Maps.newHashMap();
				return modelAndView(m, "login.ftl");
			}

		});

		post(new Route("/auth/login") {

			@Override
			public Object handle(Request request, Response response) {

				String username = request.queryParams("username");
				String password = request.queryParams("password");

				UsernamePasswordToken token = new UsernamePasswordToken(
						username, password);
				token.setRememberMe(true);
				// With most of Shiro, you'll always want to make sure you're
				// working with the currently executing user, referred to as the
				// subject
				Subject currentUser = SecurityUtils.getSubject();

				// Authenticate the subject by passing
				// the user name and password token
				// into the login method

				try {
					currentUser.login(token);
					response.redirect("/");
					halt();
				} catch (AuthenticationException e) {
					logger.info(e.toString());
					logger.debug(e.toString(),e);
				}
				response.redirect("/auth/login");
				halt();
				return null;
			}

		});

	}
/*
	public boolean isAuthenticated(Request request) {
		Subject subject = SecurityUtils.getSubject();
		System.out.println("SUBJECT: " + subject.getPrincipal());
		return (subject.getPrincipal() != null);
	}*/
	/*
	 * @Override public void init() {
	 * 
	 * before(new Filter() {
	 * 
	 * @Override public void handle(Request request, Response response) { String
	 * path = request.pathInfo(); if (path.startsWith("/admin") &&
	 * !isAuthenticated(request)) { String onLogin = request.pathInfo();
	 * request.session().attribute("onLogin", onLogin);
	 * response.redirect("/login"); halt(); }
	 * 
	 * } });
	 * 
	 * post(new MacGyverFreeMarkerRoute("/admin/console") {
	 * 
	 * @Override public Object handle(Request request, Response response) {
	 * String x = request.queryParams("scriptText"); Map<String, Object> model =
	 * Maps.newHashMap(); if (x != null) {
	 * 
	 * ScriptExecutor se = new ScriptExecutor(); try { Object val = se.eval(x,
	 * "groovy"); if (val != null) { model.put("scriptVal", val.toString()); } }
	 * catch (ScriptException e) { model.put("scriptException", e);
	 * e.printStackTrace(); } model.put("scriptOut",
	 * se.getStandardOutputString()); model.put("scriptErr",
	 * se.getStandardErrorString());
	 * 
	 * }
	 * 
	 * model.put("scriptText", x);
	 * 
	 * return modelAndView(model, "admin/console.ftl"); } });
	 * 
	 * get(new MacGyverFreeMarkerRoute("/admin/console") {
	 * 
	 * @Override public Object handle(Request request, Response response) {
	 * Map<String, Object> model = Maps.newHashMap();
	 * 
	 * return modelAndView(model, "admin/console.ftl"); }
	 * 
	 * });
	 * 
	 * get(new Route("/admin/beans") {
	 * 
	 * @Override public Object handle(Request request, Response response) {
	 * String x = ""; for (String name : getApplicationContext()
	 * .getBeanDefinitionNames()) { x = x + "<p>" + name;
	 * 
	 * }
	 * 
	 * return x; } });
	 */
}

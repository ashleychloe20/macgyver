package io.macgyver.http.spark;

import static spark.Spark.*;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import io.macgyver.core.script.ScriptExecutor;
import io.macgyver.http.spark.freemarker.MacGyverFreeMarkerRoute;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

public class AdminSparklet extends Sparklet {

	public boolean isAuthenticated(Request request) {
		Object auth = request.session().attribute("__username");
		return (auth != null);
	}

	@Override
	public void init() {

		before(new Filter() {

			@Override
			public void handle(Request request, Response response) {
				String path = request.pathInfo();
				if (path.startsWith("/admin") && !isAuthenticated(request)) {
					String onLogin = request.pathInfo();
					request.session().attribute("onLogin", onLogin);
					response.redirect("/login");
					halt();
				}

			}
		});

		post(new MacGyverFreeMarkerRoute("/admin/console") {

			@Override
			public Object handle(Request request, Response response) {
				String x = request.queryParams("scriptText");
				Map<String, Object> model = Maps.newHashMap();
				if (x != null) {

					ScriptExecutor se = new ScriptExecutor();
					try {
						Object val = se.eval(x, "groovy");
						if (val != null) {
							model.put("scriptVal", val.toString());
						}
					} catch (ScriptException e) {
						model.put("scriptException", e);
						e.printStackTrace();
					}
					model.put("scriptOut", se.getStandardOutputString());
					model.put("scriptErr", se.getStandardErrorString());

				}

				model.put("scriptText", x);

				return modelAndView(model, "admin/console.ftl");
			}
		});

		get(new MacGyverFreeMarkerRoute("/admin/console") {

			@Override
			public Object handle(Request request, Response response) {
				Map<String, Object> model = Maps.newHashMap();

				return modelAndView(model, "admin/console.ftl");
			}

		});

		get(new Route("/admin/beans") {

			@Override
			public Object handle(Request request, Response response) {
				String x = "";
				for (String name : getApplicationContext()
						.getBeanDefinitionNames()) {
					x = x + "<p>" + name;

				}

				return x;
			}
		});

		get(new Route("/login") {

			@Override
			public Object handle(Request request, Response response) {
				return "<html><body><form action='/login' method='post'>username: <input type='text' name='username'/><p/>password:<input type='password' name='password'/><p/><input type='submit' name='login'></form></body></html>";
			}

		});

		post(new Route("/login") {

			@Override
			public Object handle(Request request, Response response) {

				String username = request.queryParams("username");
				String password = request.queryParams("password");
	

				if ("scott".equals(username) && "tiger".equals(password)) {
					request.session().attribute("__username", username);
					String onLogin = request.session().attribute("onLogin");

					if (!Strings.isNullOrEmpty(onLogin)) {
						response.redirect(onLogin);
						halt();
					} else {
						response.redirect("/");
						halt();
					}
				} else {
					response.redirect("/login");
					halt();
				}
				return null;
			}

		});

	}

}

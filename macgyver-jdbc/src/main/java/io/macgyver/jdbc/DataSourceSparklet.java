package io.macgyver.jdbc;

import static spark.Spark.get;
import io.macgyver.http.spark.Sparklet;
import io.macgyver.http.spark.freemarker.MacGyverFreeMarkerRoute;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import spark.Request;
import spark.Response;

import com.google.common.collect.Maps;

public class DataSourceSparklet extends Sparklet {

	@Autowired
	DataSourceFactory dataSourceFactory;

	@Override
	public void init() {

		
		get(new MacGyverFreeMarkerRoute("/jdbc/datasources") {

			@Override
			public Object handle(Request request, Response response) {
				
				Map<String, Object> m = Maps.newHashMap();
				m.put("dataSourceFactory", dataSourceFactory);
				return modelAndView(m, "jdbcDataSources.ftl");
			}
		});
	}

}

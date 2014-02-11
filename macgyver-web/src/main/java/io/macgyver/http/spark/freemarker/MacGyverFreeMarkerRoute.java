package io.macgyver.http.spark.freemarker;

import io.macgyver.core.Kernel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerRoute;

public abstract class MacGyverFreeMarkerRoute extends FreeMarkerRoute {

	Logger logger = LoggerFactory.getLogger(MacGyverFreeMarkerRoute.class);
	
	protected MacGyverFreeMarkerRoute(String path) {
		super(path);
		setConfiguration(createConfiguration());
	}

	protected Configuration createConfiguration() {
	
			Configuration cfg = new Configuration();
			
			
			
			File templateDir = new File(Kernel.getInstance().getExtensionDir(),"web").getAbsoluteFile();
			logger.info("templates dir: {}",templateDir.getAbsolutePath());
			
			List<TemplateLoader> list = Lists.newArrayList();
			
			try {
				if (!templateDir.exists()) {
					logger.info("attempting to create template dir: {}",templateDir.getAbsolutePath());
					templateDir.mkdirs();
				}
				FileTemplateLoader ftl = new FileTemplateLoader(templateDir);
				list.add(ftl); // This should be first in list
		
			}
			catch (IOException e) {
				logger.warn("template dir could not be found: "+templateDir.getAbsolutePath());
			}
			
			
			ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(),"/io/macgyver/web");
			
			
			
			list.add(ctl);
			
			MultiTemplateLoader mtl = new MultiTemplateLoader(list.toArray(new TemplateLoader[0]));
			cfg.setTemplateLoader(mtl);
			return cfg;
		

	}

	public MacGyverFreeMarkerRoute(String path, String acceptType) {
		super(path, acceptType);
		setConfiguration(createConfiguration());
	}

}

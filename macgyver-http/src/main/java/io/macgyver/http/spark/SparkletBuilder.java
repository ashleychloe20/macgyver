package io.macgyver.http.spark;

import io.macgyver.core.Kernel;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import spark.Spark;
import spark.servlet.SparkApplication;
import static spark.Spark.*;

public class SparkletBuilder implements SparkApplication{

	Logger logger = LoggerFactory.getLogger(SparkletBuilder.class);
	
	@Override
	public void init() {
	
		ApplicationContext applicationContext = Kernel.getInstance().getApplicationContext();
		Map<String,Sparklet> m = applicationContext.getBeansOfType(Sparklet.class);
	
		for (Map.Entry<String,Sparklet> entry: m.entrySet()) {
			SparkApplication app = entry.getValue();
			if (app==this) {
				logger.info("skipping self: {}",app);
				
			}
			else {
				try {
					logger.info("{}.init()",app);
					app.init();
				}
				finally {}
			}
			
		}
	}

}

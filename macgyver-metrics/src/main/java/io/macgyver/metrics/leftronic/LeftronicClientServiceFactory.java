package io.macgyver.metrics.leftronic;

import groovy.util.ConfigObject;
import io.macgyver.core.ServiceFactory;

import java.util.Properties;

import javax.json.JsonObject;

import com.google.common.base.Optional;

public class LeftronicClientServiceFactory extends ServiceFactory<LeftronicClient> {

	
	public LeftronicClientServiceFactory() {
		super("leftronic");
	
	}

	@Override
	public LeftronicClient create(String name, JsonObject cfg) {
		

		String accessKey = cfg.getString("accessKey");
		return new LeftronicClient(accessKey);
	}

}

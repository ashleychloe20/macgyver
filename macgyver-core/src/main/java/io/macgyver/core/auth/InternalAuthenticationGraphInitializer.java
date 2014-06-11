package io.macgyver.core.auth;

import io.macgyver.core.graph.TitanGraphInitailizer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.thinkaurelius.titan.core.TitanGraph;

public class InternalAuthenticationGraphInitializer extends TitanGraphInitailizer {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	InternalUserManager userManager;
	
	public InternalAuthenticationGraphInitializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doInitTitanGraphMetadata(TitanGraph g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doInitTitanGraphData(TitanGraph g) {
	
			
			Optional<InternalUser> admin = userManager.getInternalUser("admin");
			if (admin.isPresent()) {
				logger.debug("admin user already exists");
			}
			else {
				logger.info("adding admin user");
				List<String> roleList = Lists.newArrayList("ROLE_MACGYVER_SHELL","ROLE_MACGYVER_UI", "ROLE_MACGYVER_ADMIN","ROLE_MACGYVER_USER");
				
				userManager.createUser("admin", roleList);
				userManager.setPassword("admin", "admin");
			}
			

		
	}

}

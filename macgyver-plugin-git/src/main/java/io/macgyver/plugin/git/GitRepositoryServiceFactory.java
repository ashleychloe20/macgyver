package io.macgyver.plugin.git;

import java.util.Properties;
import java.util.Set;

import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;

public class GitRepositoryServiceFactory extends ServiceFactory<GitRepository>{

	public GitRepositoryServiceFactory() {
		super("GitRepository");
		
	}

	@Override
	protected GitRepository doCreateInstance(ServiceDefinition def) {
		
		GitRepository repo = new GitRepository();
		Properties p = def.getProperties();
		repo.setUrl(p.getProperty("url"));
		repo.setUsername(p.getProperty("username"));
		repo.setPassword(p.getProperty("password"));
		return repo;
	}

	@Override
	protected void doCreateCollaboratorInstances(ServiceRegistry registry,
			ServiceDefinition primaryDefinition, GitRepository primaryBean) {
		
		
	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {
	
		
	}



}

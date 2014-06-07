package io.macgyver.plugin.git;

import java.util.Properties;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;

import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceFactory;
import io.macgyver.core.service.ServiceRegistry;

public class GitRepositoryServiceFactory extends ServiceFactory<GitRepository>{

	@Autowired
	ExtensionResourceProvider provider;
	
	public GitRepositoryServiceFactory() {
		super("GitRepository");
		
	}

	@Override
	public void doConfigureDefinition(ServiceDefinition def) {
		super.doConfigureDefinition(def);
		logger.info("Configuring: "+def);
	
		Properties p = def.getProperties();
		boolean isResourceProvider = Boolean.parseBoolean(p.getProperty("resourceProvider", "false"));
		if (isResourceProvider) {
			def.setLazyInit(false);

		}
		
	}

	@Override
	protected GitRepository doCreateInstance(ServiceDefinition def) {
		
		GitRepository repo = new GitRepository();
		Properties p = def.getProperties();
		repo.setUrl(p.getProperty("url"));
		repo.setUsername(p.getProperty("username"));
		repo.setPassword(p.getProperty("password"));
		
		if (Boolean.parseBoolean(p.getProperty("resourceProvider","false"))) {
			logger.info("using {} as macgyver extension for {}",def,provider);
			
			GitResourceProvider rp = new GitResourceProvider(repo);
			int fetchInterval = Integer.parseInt(p.getProperty("resourceProviderFetchInterval","300"));
			String ref = p.getProperty("resourceProviderGitRef","refs/heads/master");
			rp.setGitRef(ref);
			rp.setFetchIntervalSecs(fetchInterval);
			provider.replaceResourceProvider(rp);
	
		}
		
		
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

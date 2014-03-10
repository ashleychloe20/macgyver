package io.macgyver.chat.hipchat;

import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceRegistry;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ning.http.client.AsyncHttpClient;

public class HipChatServiceFactory extends
		io.macgyver.core.service.ServiceFactory<HipChat> {

	@Autowired
	@Qualifier("macgyverAsyncHttpClient")
	AsyncHttpClient client;

	public HipChatServiceFactory() {
		super("hipchat");

	}

	@Override
	protected HipChat doCreateInstance(ServiceDefinition def) {
		HipChat c = new HipChat(client);
		c.setApiKey(def.getProperties().getProperty("apiKey"));
		return c;
	}

	@Override
	protected void doCreateCollaboratorInstances(ServiceRegistry registry,
			ServiceDefinition primaryDefinition, HipChat primaryBean) {
		// Not required
	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {
		// Not required
	}

}

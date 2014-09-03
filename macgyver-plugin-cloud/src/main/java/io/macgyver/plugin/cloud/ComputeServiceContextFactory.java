package io.macgyver.plugin.cloud;

import io.macgyver.core.MacGyverException;
import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.base.Strings;

@Component
public class ComputeServiceContextFactory extends
		BasicServiceFactory<ComputeServiceContext> {

	public static final String CERTIFICATE_VERIFICATION_DEFAULT = "false";

	public ComputeServiceContextFactory() {
		super("jclouds-compute");
	}

	@Override
	protected ComputeServiceContext doCreateInstance(ServiceDefinition def) {

		Properties props = def.getProperties();

		String provider = props.getProperty("provider");

		String username = props.getProperty("username");
		if (username != null) {
			props.put(provider + ".identity", username);
		}

		String url = props.getProperty("url");
		if (url != null) {
			props.put(provider + ".endpoint", url);
		}

		String password = props.getProperty("password");
		if (password != null) {
			props.put(provider + ".credential", password);
		}

		Preconditions.checkArgument(!Strings.isNullOrEmpty(provider),
				"provider must be specified");
		return ContextBuilder
				.newBuilder(provider)
				.credentials(username, "xx")
				// .endpoint(url)
				.overrides(props)
				.modules(
						ImmutableSet.of(new SLF4JLoggingModule(),
								new SshjSshClientModule()))
				.build(ComputeServiceContext.class);

	}
}

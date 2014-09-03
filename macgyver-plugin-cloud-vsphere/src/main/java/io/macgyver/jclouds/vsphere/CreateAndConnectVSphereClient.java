package io.macgyver.jclouds.vsphere;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.rmi.RemoteException;

import javax.inject.Inject;

import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.Credentials;
import org.jclouds.location.Provider;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.vmware.vim25.mo.ServiceInstance;

@javax.inject.Singleton
public class CreateAndConnectVSphereClient implements Supplier<VSphereServiceInstance> {

	private final Supplier<URI> providerSupplier;

	private transient Supplier<Credentials> credentials;

	@Inject
	public CreateAndConnectVSphereClient(
			Function<Supplier<NodeMetadata>, ServiceInstance> providerContextToCloud,

			@Provider Supplier<URI> providerSupplier,
			@Provider Supplier<Credentials> credentials) {

		this.credentials = checkNotNull(credentials, "credentials is needed");
		this.providerSupplier = checkNotNull(providerSupplier,
				"endpoint to vSphere node or vCenter server is needed");
	}

	public synchronized ServiceInstance start() {
		URI provider = providerSupplier.get();
		try {
			return new ServiceInstance(new URL(provider.toASCIIString()),
					credentials.get().identity, credentials.get().credential,
					true);
		} catch (RemoteException e) {
			throw Throwables.propagate(e);
		} catch (MalformedURLException e) {
			throw Throwables.propagate(e);
		}
	}

	@Override
	public VSphereServiceInstance get() {
		ServiceInstance client = start();
		Preconditions.checkState(client != null,
				"(CreateAndConnectVSphereClient) start not called");
		return new VSphereServiceInstance(client);
	}
}

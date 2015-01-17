package io.macgyver.plugin.cloud.vsphere;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import net.sf.cglib.proxy.Enhancer;

import org.springframework.aop.framework.ProxyFactory;

import com.thoughtworks.proxy.toys.hotswap.HotSwapping;
import com.thoughtworks.proxy.toys.hotswap.Swappable;
import com.vmware.vim25.mo.ServerConnection;
import com.vmware.vim25.mo.ServiceInstance;

/**
 * 
 * @author rschoening
 *
 */
public class RefreshingServiceInstance extends ServiceInstance {

	private URL retainedUrl;
	private String retainedUsername;
	private String retainedPassword;
	private boolean retainedIgnoreCert;
	
	public RefreshingServiceInstance() {
		super(null);
	}

	public RefreshingServiceInstance(URL url, String username, String password, boolean ignore) throws MalformedURLException, RemoteException {
		super(url,username,password,ignore);
		this.retainedUrl = url;
		this.retainedIgnoreCert = ignore;
		this.retainedUsername = username;
		this.retainedPassword = password;
	}
	
	
	public URL getRetainedUrl() {
		return retainedUrl;
	}
	public String getRetainedUsername() {
		return retainedUsername;
	}
	public String getRetainedPassword() {
		return retainedPassword;
	}
	public boolean getRetainedIgnoreCert() {
		return retainedIgnoreCert;
	}
	
	/**
	 * This method needs to be static or else it gets called on the delegate, which doesn't work.
	 * @param proxy
	 * @throws IOException
	 */
	public static void refreshToken(RefreshingServiceInstance proxy) throws IOException {
		// This needs to be static or else it gets called on the delegate which doesn't work!
		
		

		RefreshingServiceInstance si = new RefreshingServiceInstance(proxy.getRetainedUrl(), proxy.getRetainedUsername(), proxy.getRetainedPassword(), proxy.getRetainedIgnoreCert());
		Swappable.class.cast(proxy).hotswap(si);
		
	}
}

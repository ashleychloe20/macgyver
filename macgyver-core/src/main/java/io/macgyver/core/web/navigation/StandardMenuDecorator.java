package io.macgyver.core.web.navigation;


public class StandardMenuDecorator implements MenuDecorator {

	@Override
	public void decorate(Menu root) {
		
		root.addMenuItem("admin", "/admin/encryptString", "Encrypt String");
		root.addMenuItem("admin", "/admin/beans", "Beans");
		root.addMenuItem("admin", "/admin/scripts", "Scripts");
		root.addMenuItem("admin", "/admin/refreshResourceProvider", "Refresh Resources");
		
		root.setDisplayName("admin","Admin");

	}

}

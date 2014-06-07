package io.macgyver.core.web.navigation;


public class StandardMenuDecorator implements MenuDecorator {

	@Override
	public void decorate(MenuItem root) {
		
		
		MenuItem admin = root.getChildById("admin", true);
		admin.setDisplayName("Admin");
		
		MenuItem encrypt = admin.getChildById("encrypt-string", true);
		encrypt.setDisplayName("Encrypt String");
		encrypt.setUriPath("/admin/encryptString");
		
		MenuItem beans = admin.getChildById("beans", true);
		beans.setDisplayName("Beans");
		beans.setUriPath("/admin/beans");
		
		MenuItem refresh = admin.getChildById("refreshResourceProvider", true);
		refresh.setDisplayName("Refresh Resources");
		refresh.setUriPath("/admin/refreshResourceProvider");
	//	MenuItem script = admin.getChildById("script-eval", true);
	//	script.setDisplayName("Eval Script");
	//	script.setUriPath("/admin/script");
		
		
	
		
	
		

	}

}

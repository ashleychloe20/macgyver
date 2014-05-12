package io.macgyver.core.web.navigation;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

		
		MenuItem script = admin.getChildById("script-eval", true);
		script.setDisplayName("Eval Script");
		script.setUriPath("/admin/script");
		
		
	
		
	
		

	}

}

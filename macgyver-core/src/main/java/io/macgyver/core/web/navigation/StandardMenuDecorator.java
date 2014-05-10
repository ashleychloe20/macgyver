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
		

		
		
		
		
	
		
	
		

	}

}

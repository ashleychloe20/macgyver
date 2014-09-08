package io.macgyver.core.web.vaadin;

import com.vaadin.data.Container;

public class VaadinUtil {

	public static boolean isEmpty(Container t) {
		if (t==null) {
			return true;
		}

		try {
			return t.getItemIds().isEmpty();
		}
		catch (Exception e) {
			
		}
		return true;
	}
}

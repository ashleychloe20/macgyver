package io.macgyver.core.resource;

public class ResourceMatchers {

	public static ResourceMatcher matchAll() {
		ResourceMatcher rm = new ResourceMatcher() {
			
			@Override
			public boolean matches(Resource r) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		return rm;
	}

}

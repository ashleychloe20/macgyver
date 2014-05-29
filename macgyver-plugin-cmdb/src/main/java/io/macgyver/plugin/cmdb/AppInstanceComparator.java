package io.macgyver.plugin.cmdb;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class AppInstanceComparator implements Comparator<AppInstance> {


	
	@Override
	public int compare(AppInstance o1, AppInstance o2) {
		if (o1==null && o2 == null) {
			return 0;
		}
		if (o1==null && o2 !=null) {
			return -1;
		}
		if (o1!=null && o2 == null) {
			return 1;
		}
		int cv = Objects.toString(o1.getProfile()).compareToIgnoreCase(Objects.toString(o2.getProfile()));
		if (cv != 0) {
			return cv;
		}
		cv = Objects.toString(o1.getGroupId()).compareToIgnoreCase(Objects.toString(o2.getGroupId()));
		if (cv != 0) {
			return cv;
		}
		cv = Objects.toString(o1.getArtifactId()).compareToIgnoreCase(Objects.toString(o2.getArtifactId()));
		if (cv != 0) {
			return cv;
		}
		cv = Objects.toString(o1.getHost()).compareToIgnoreCase(Objects.toString(o2.getHost()));
		if (cv != 0) {
			return cv;
		}
		
		return 0;
	}

}

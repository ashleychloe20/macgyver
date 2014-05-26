package io.macgyver.plugin.cmdb;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class AppInstanceComparator implements Comparator<AppInstance> {

	static class Order {
		String key;
		int order = 1;
	}

	List<Order> orderList = Lists.newArrayList();

	public void sortDescending(String key) {
		Order ord = new Order();
		ord.key = key;
		ord.order=-1;
		orderList.add(ord);
	}
	public void sortAscending(String key) {
		Order ord = new Order();
		ord.key = key;
		ord.order=1;
		orderList.add(ord);
	}
	
	@Override
	public int compare(AppInstance o1, AppInstance o2) {
		for (Order order : orderList) {
			int rval = 0;
			Object c1 = o1.getProperties().get(order.key);
			Object c2 = o2.getProperties().get(order.key);
			if (c1 == null) {
				c1 = "";
			}
			if (c2 == null) {
				c2 = "";
			}

			rval = c1.toString().compareTo(c2.toString());
			if (rval != 0) {
				return rval * order.order;
			}

		}
		return 0;
	}

}

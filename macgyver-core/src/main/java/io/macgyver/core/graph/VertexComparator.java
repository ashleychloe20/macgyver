package io.macgyver.core.graph;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Vertex;

public class VertexComparator implements Comparator<Vertex> {
	public static Logger logger = LoggerFactory.getLogger(VertexComparator.class);
	
	public class SortOrder {
		String key;
		int direction = 1;
	}

	List<SortOrder> sortOrderList = Lists.newArrayList();

	public VertexComparator() {

	}

	public void sortAscending(String key) {
		Preconditions.checkNotNull(key);
		SortOrder so = new SortOrder();
		so.key = key;
		so.direction = 1;
		sortOrderList.add(so);
	}

	public void sortDescending(String key) {
		Preconditions.checkNotNull(key);
		SortOrder so = new SortOrder();
		so.key = key;
		so.direction = -1;
		sortOrderList.add(so);
	}

	@Override
	public int compare(Vertex o1, Vertex o2) {
		try {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null && o2 != null) {
			return -1;
		} else if (o1!= null && o2 == null) {
			return 1;
		}

		for (SortOrder so : sortOrderList) {
			Object v1 = o1.getProperty(so.key);
			Object v2 = o2.getProperty(so.key);
			if (v1 instanceof String) {
				int s = ((String) v1).compareToIgnoreCase(Objects.toString(v2));
				s = s * so.direction;
				return s;
			}
			if (v1 instanceof Number) {
				double d1 = ((Number) v1).doubleValue();
				if (v2 instanceof Number) {
					double d2 = ((Number) v2).doubleValue();
					if (d1 == d2) {
						return 0;
					} else if (d1 < d2) {
						return -1 * so.direction;
					} else if (d1 > d2) {
						return so.direction;
					}
				} else {
					return so.direction;
				}
			} else {
				String s1 = Objects.toString(v1, "");
				String s2 = Objects.toString(v2, "");
				return s1.compareToIgnoreCase(s2);
			}
			return 0;
		}
		return 0;
		}
		catch (RuntimeException e) {
			logger.warn("logic error in compareTo()",e);
			return 0;
		}
	}
}

package io.macgyver.core.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class JsonNodes {

	public static void sort(List<JsonNode> n, Comparator<JsonNode> comparator) {
		Preconditions.checkNotNull(n);
		Preconditions.checkNotNull(comparator);
		Collections.sort(n, comparator);
	}

	public static void sort(ArrayNode array, Comparator<JsonNode> comparator) {
		Preconditions.checkNotNull(array);
		Preconditions.checkNotNull(comparator);
		List<JsonNode> arr = Lists.newArrayList();
		for (JsonNode n : array) {
			arr.add(n);
		}
		Collections.sort(arr, comparator);
		array.removeAll();
		for (JsonNode n : arr) {
			array.add(n);
		}
	}

	public static class PropertyComparator implements Comparator<JsonNode> {

		String propertyName;
		boolean caseSensitive = true;

		public PropertyComparator(String s, boolean caseSensitive) {
			this.propertyName = s;
			this.caseSensitive = caseSensitive;
		}

		@Override
		public int compare(JsonNode o1, JsonNode o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}

			JsonNode n1 = o1.path(propertyName);
			JsonNode n2 = o2.path(propertyName);

			if (n1.isNumber() || n2.isNumber()) {
				if (n1.isNull()) {
					return new Double(0).compareTo(n2.asDouble());
				} else if (n2.isNull()) {
					return new Double(n2.asDouble()).compareTo(0d);
				}
				if (n1.isNumber() && n2.isNumber()) {
					double d1 = n1.asDouble();
					double d2 = n2.asDouble();
					if (d1 < d2) {
						return -1;
					}
					if (d1 > d2) {
						return 1;
					}
					return 0;
				}
			}
			String s1 = n1.asText();
			String s2 = n2.asText();
			if (caseSensitive) {
				return s1.compareTo(s2);
			} else {
				return s1.compareToIgnoreCase(s2);
			}
		}

	}

	public static class TextComparator implements Comparator<JsonNode> {

		String propertyName;
		boolean caseSensitive = true;

		public TextComparator(String s, boolean caseSensitive) {
			this.propertyName = s;
			this.caseSensitive = caseSensitive;
		}

		@Override
		public int compare(JsonNode o1, JsonNode o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}

			String s1 = o1.path(propertyName).asText();
			String s2 = o2.path(propertyName).asText();
			if (s1 == null) {
				s1 = "";
			}
			if (s2 == null) {
				s2 = "";
			}
			if (caseSensitive) {
				return s1.compareTo(s2);
			} else {
				return s1.compareToIgnoreCase(s2);
			}
		}

	}

	public static class NumericComparator implements Comparator<JsonNode> {

		String propertyName;
		Number defaultValue = 0;

		public NumericComparator(String s, Number defaultValue) {
			this.propertyName = s;
			if (defaultValue == null) {
				defaultValue = 0;
			} else {
				this.defaultValue = defaultValue;
			}
		}

		@Override
		public int compare(JsonNode o1, JsonNode o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}

			Double s1 = o1.path(propertyName).asDouble(
					defaultValue.doubleValue());
			Double s2 = o2.path(propertyName).asDouble(
					defaultValue.doubleValue());

			return s1.compareTo(s2);
		}

	}

	public static Comparator<JsonNode> numericComparator(String prop) {
		return new NumericComparator(prop, 0);
	}

	public static Comparator<JsonNode> textComparator(String prop) {
		return new TextComparator(prop, false);
	}

	public static Comparator<JsonNode> textComparator(String prop,
			boolean caseSenstitive) {

		return new TextComparator(prop, caseSenstitive);

	}
}

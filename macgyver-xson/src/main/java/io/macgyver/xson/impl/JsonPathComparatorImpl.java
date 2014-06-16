package io.macgyver.xson.impl;

import io.macgyver.xson.Xson;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class JsonPathComparatorImpl implements Comparator<Object> {

	boolean caseSensitive=false;
	
	List<SortArg>  sortArgList = Lists.newArrayList();
	
	public static class SortArg {
		String jsonPath;
		Xson.SortOrder order;
	}
	public JsonPathComparatorImpl() {
		
		
	}
	public void setCaseSensitive(boolean b) {
		this.caseSensitive =b;
	}
	public boolean isCaseSensitive() {
		return this.caseSensitive;
	}
	public void addSortOrder(String path, Xson.SortOrder order) {
		SortArg arg = new SortArg();
		arg.jsonPath = path;
		arg.order = order;
		sortArgList.add(arg);
	}
	@Override
	public int compare(Object o1, Object o2) { 
		
		// This allows for secondary and tertiary sorting
		int val = 0;
		for (SortArg arg: sortArgList) {
			
			val = compare(arg.jsonPath,o1,o2);
			if (arg.order==Xson.SortOrder.DESCENDING) {
				val = val * -1;
			}
			if (val!=0) {
				return val;
			}
		}
		
		return val;
	
	}
	protected int compare(String jsonPath, Object o1, Object o2) {
		Object v1 = Xson.eval(o1, jsonPath);
		Object v2 = Xson.eval(o2, jsonPath);
		
		if (v1==null) {
			v1="";
		}
		if (v2==null) {
			v2="";
		}
		
		if (v1 instanceof Number && v2 instanceof Number) {
			Number n1 = (Number)v1;
			Number n2 = (Number)v2;
			return new Double(n1.doubleValue()).compareTo(new Double(n2.doubleValue()));		
		}
		
		String s1 = v1.toString();
		String s2 = v2.toString();
		
		if (!caseSensitive) {
			s1 = s1.toLowerCase();
			s2 = s2.toLowerCase();
		}

		return s1.compareTo(s2);
	
	}

}

package io.macgyver.core.web.w2ui;

import io.macgyver.xson.Xson;
import io.macgyver.xson.Xson.SortOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class GridDataControl {

	GridDataRequest gridRequest;

	ObjectMapper mapper = new ObjectMapper();

	ArrayList<ObjectNode> results = Lists.newArrayList();
	
	ArrayList<ObjectNode> pagedResults=null;
	
	int totalResults = -1;

	boolean caseSensitive=false;

	public GridDataControl(HttpServletRequest request) {
		Preconditions.checkNotNull(request);
		gridRequest = new GridDataRequest(request);
	}

	public void setDefaultSort(String n) {
		gridRequest.setDefaultSort(n);
	}

	public void addRow(ObjectNode n) {
		results.add(n);
	}
	protected void populate() {}
	
	public ObjectNode process() {
		
		populate();
		applyFilter();
		applySort();
		applyLimitAndOffset();

		ObjectNode r = mapper.createObjectNode();
		r.put("status", "success");
		r.put("total", totalResults >= 0 ? totalResults : results.size());
		ArrayNode arr = mapper.createArrayNode();
		for (ObjectNode n : pagedResults) {
			arr.add(n);
		}
		r.put("records", arr);

		return r;

	}

	protected void applyFilter() {
		applyFilter(gridRequest);
	}

	protected void applyFilter(GridDataRequest request) {
		String searchFilter = request.getServletRequest().getParameter(
				"search[0][value]");
		List<ObjectNode> filteredList;

		if (searchFilter != null) {
			Iterator<ObjectNode> t = results.iterator();
			while (t.hasNext()) {
				ObjectNode n = t.next();
				if (!anyFieldContains(n, searchFilter)) {
					t.remove();
				}
			}
		}
	}

	boolean anyFieldContains(ObjectNode n, String filter) {
		Iterator<Entry<String, JsonNode>> t = n.fields();

		filter=filter.trim();
		if (!caseSensitive) {
			filter = filter.toLowerCase();
		}
		
		while (t.hasNext()) {
			
			String val = Strings.nullToEmpty(t.next().getValue().asText()).trim();
			if (!caseSensitive) {
				val=val.toLowerCase();
			}
			if (val.contains(filter)) {
				return true;
			}
		}
		return false;
	}

	protected void applySort() {
		applySort(gridRequest);
	}

	protected void applyLimitAndOffset() {
		applyLimitAndOffset(gridRequest);
	}

	protected void applyLimitAndOffset(GridDataRequest gridRequest) {
		totalResults = results.size();
		int recid = 1; // w2ui starts with index=1
		for (ObjectNode n : results) {
			n.put("recid", recid);
			recid++;
		}
		int offset = Math.min(Math.max(0, gridRequest.getOffset()),
				Math.max(0, results.size() - 1));
		int toIndex = Math.min(offset + Math.max(gridRequest.getLimit(), 0),
				results.size());

		pagedResults = Lists.newArrayList(results.subList(offset,
				toIndex)); // deep copy required
		
	}

	protected void applySort(GridDataRequest request) {
		List<Sort> sortList = request.getSortOrder();
		if (sortList.isEmpty()) {
			return;
		}
		Sort sort = sortList.get(0);

		Comparator comparator = null;

		if (sort.getDirection() == Sort.Direction.ASCENDING) {
			comparator = Xson.pathComparator("$." + sort.getField(),
					SortOrder.ASCENDING);
		} else {
			comparator = Xson.pathComparator("$." + sort.getField(),
					SortOrder.DESCENDING);
		}

		Collections.sort(results, comparator);

	}

}

package io.macgyver.core.web.w2ui;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

public class GridDataRequest {

	Logger logger = org.slf4j.LoggerFactory.getLogger(GridDataRequest.class);

	HttpServletRequest request;

	List<Sort> defaultSort = Lists.newArrayList();

	public GridDataRequest(HttpServletRequest r) {
		this.request = r;

		if (logger.isDebugEnabled()) {
			Enumeration<String> e = r.getParameterNames();
			while (e.hasMoreElements()) {
				String x = e.nextElement();
				String v = r.getParameter(x);
				logger.info("param: {}={}", x, v);
			}
		}
	}

	public HttpServletRequest getServletRequest() {
		return request;
	}

	public String getParameter(String name) {
		return request.getParameter(name);
	}

	public String getCommand() {
		return request.getParameter("cmd");
	}

	public int getLimit() {
		return Integer.parseInt(request.getParameter("limit"));
	}

	public int getOffset() {
		return Integer.parseInt(request.getParameter("offset"));
	}

	public boolean isGetRecordsRequest() {
		return "get-records".equals(getCommand());
	}

	public void setDefaultSort(String sort) {
		defaultSort = Lists.newArrayList(new Sort(sort, Sort.Direction.asc));
	}

	public List<Sort> getSortOrder() {
		List<Sort> list = Lists.newArrayList();

		for (int i = 0; i < 10; i++) {
			String sortFieldParam = String.format("sort[%d][field]", i);
			String sortFieldVal = request.getParameter(sortFieldParam);
			String sortDirectionParam = String.format("sort[%d][direction]", i);
			String sortDirection = request.getParameter(sortDirectionParam);
			if (sortFieldVal != null && sortDirection != null) {
				list.add(new Sort(sortFieldVal, Sort.Direction
						.valueOf(sortDirection)));

			} else {
				break;
			}
		}

		if (list.isEmpty()) {
			list.addAll(defaultSort);
		}
		return list;
	}
}

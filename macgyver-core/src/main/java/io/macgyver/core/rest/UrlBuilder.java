package io.macgyver.core.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

public class UrlBuilder {

	String base;

	List<String> parts = Lists.newArrayList();

	Map<String, String> queryParameters = Maps.newHashMap();

	public UrlBuilder() {

	}

	public UrlBuilder(String baseUrl) {
		this.base = baseUrl;
	}

	public UrlBuilder base(String base) {
		this.base = base;
		return this;
	}

	public UrlBuilder path(String p) {
		if (p != null && p.length() > 0) {
			parts.add(p);
		}
		return this;
	}

	public UrlBuilder queryParam(String key, String val) {
		queryParameters.put(key, val);
		return this;
	}

	protected String encodePathPart(String p) {
		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < p.length(); i++) {

				if (p.charAt(i) == '/') {
					sb.append(p.charAt(i));
				} else {
					sb.append(URLEncoder.encode(
							Character.toString(p.charAt(i)), "UTF-8"));

				}
			}

			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			throw new RestException(e);
		}
	}

	public UrlBuilder queryParam(Map<String, String> q) {
		UrlBuilder b = this;

		if (q != null) {
			for (Map.Entry<String, String> entry : q.entrySet()) {
				b = b.queryParam(entry.getKey(), entry.getValue());
			}
		}
		return b;
	}

	public String build() {

		Preconditions.checkState(base != null, "base() must be called");
		String url = base;
		try {
			for (String append : parts) {
				append = encodePathPart(append);
				while (url.endsWith("/") && url.length() > 0) {
					url = url.substring(0, url.length() - 1);
				}
				while (append.startsWith("/") && append.length() > 0) {
					append = append.substring(1);
				}
				url = url + "/" + append;

			}

			if (queryParameters != null && !queryParameters.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				int count = 0;
				for (Map.Entry<String, String> entry : queryParameters
						.entrySet()) {
					String key = entry.getKey();
					String val = entry.getValue();
					if (count++ > 0) {
						sb.append("&");
					}

					sb.append(URLEncoder.encode(key, "UTF-8"));
					sb.append("=");
					sb.append(URLEncoder.encode(val, "UTF-8"));

				}

				if (url.contains("?")) {
					url = url + "&" + sb.toString();
				} else {
					url = url + "?" + sb.toString();
				}
			}

			return url;
		} catch (UnsupportedEncodingException e) {
			throw new RestException(e);
		}

	}

}

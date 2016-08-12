package com.erosnow.search.common.util;

import org.springframework.util.StringUtils;

import com.google.common.util.concurrent.RateLimiter;

public enum RequestClientType {

	DEFAULT("default");

	private RequestClientType(String val) {
		this.value = val;
		this.limit = Integer.parseInt(SearchPropertyEnum.SEARCH_API_DEFAULT_LIMIT.getValue());
		this.rateLimiter = RateLimiter.create(this.limit);
	}

	private String value;

	private RateLimiter rateLimiter;

	private Integer limit;

	public static RequestClientType get(String client) {
		if (StringUtils.isEmpty(client)) {
			return DEFAULT;
		}
		for (RequestClientType reqClient : values()) {
			if (reqClient.value.equals(client)) {
				return reqClient;
			}
		}
		return DEFAULT;
	}

	public String getValue() {
		return value;
	}

	public void setRateLimiter(RateLimiter rateLimiter) {
		this.rateLimiter = rateLimiter;
	}

	public RateLimiter getRateLimiter() {
		return rateLimiter;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getLimit() {
		return limit;
	}

}

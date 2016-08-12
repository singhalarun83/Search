package com.erosnow.search.base.model.ext;

import com.erosnow.search.base.model.common.ServiceResponse;

public class SearchResponse extends ServiceResponse {
	private static final long serialVersionUID = 6133155019658510991L;

	public SearchResponse(final String code, final String message) {
		super(code, message);
	}

	public SearchResponse(final String code) {
		super(code);
	}

	public SearchResponse() {

	}

	private SearchSRO searchSRO;

	public SearchSRO getSearchSRO() {
		return searchSRO;
	}

	public void setSearchSRO(SearchSRO searchSRO) {
		this.searchSRO = searchSRO;
	}

}

package com.erosnow.search.base.model.ext;

import com.erosnow.search.base.model.common.ServiceRequest;

public class SearchRequest extends ServiceRequest {
	private static final long serialVersionUID = -4930390865128090687L;

	protected String q;

	protected String keyword;

	protected int start;

	protected int number;

	protected String sortBy;

	private String requestClient;

	private boolean debug;

	private boolean spellCheckEnabled;

	private boolean partialSearchEnabled;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getRequestClient() {
		return requestClient;
	}

	public void setRequestClient(String requestClient) {
		this.requestClient = requestClient;
	}

	public boolean isSpellCheckEnabled() {
		return spellCheckEnabled;
	}

	public void setSpellCheckEnabled(boolean spellCheckEnabled) {
		this.spellCheckEnabled = spellCheckEnabled;
	}

	public boolean isPartialSearchEnabled() {
		return partialSearchEnabled;
	}

	public void setPartialSearchEnabled(boolean partialSearchEnabled) {
		this.partialSearchEnabled = partialSearchEnabled;
	}

}

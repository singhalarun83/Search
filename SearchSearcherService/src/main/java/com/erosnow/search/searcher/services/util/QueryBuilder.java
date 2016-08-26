package com.erosnow.search.searcher.services.util;

import com.erosnow.search.base.util.SortCategory;

public class QueryBuilder {

	private SortCategory sortBy;

	private Integer number;

	private String q;

	private String keyword;

	private int start;

	private String requestClient;

	private boolean debug;

	private boolean spellCheckEnabled;

	private boolean partialSearchEnabled;

	private String searchTerm;

	private String[] fl;

	private String[] qf;

	private String[] fq;

	public String getQ() {
		return q;
	}

	public QueryBuilder setQ(String q) {
		this.q = q;
		return this;
	}

	public String getKeyword() {
		return keyword;
	}

	public QueryBuilder setKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public int getStart() {
		return start;
	}

	public QueryBuilder setStart(int start) {
		this.start = start;
		return this;
	}

	public String getRequestClient() {
		return requestClient;
	}

	public QueryBuilder setRequestClient(String requestClient) {
		this.requestClient = requestClient;
		return this;
	}

	public boolean isDebug() {
		return debug;
	}

	public QueryBuilder setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public boolean isSpellCheckEnabled() {
		return spellCheckEnabled;
	}

	public QueryBuilder setSpellCheckEnabled(boolean spellCheckEnabled) {
		this.spellCheckEnabled = spellCheckEnabled;
		return this;
	}

	public SortCategory getSortBy() {
		return sortBy;
	}

	public QueryBuilder setSortBy(SortCategory sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public Integer getNumber() {
		return number;
	}

	public QueryBuilder setNumber(Integer number) {
		this.number = number;
		return this;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public QueryBuilder setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
		return this;
	}

	public boolean isPartialSearchEnabled() {
		return partialSearchEnabled;
	}

	public void setPartialSearchEnabled(boolean partialSearchEnabled) {
		this.partialSearchEnabled = partialSearchEnabled;
	}

	public String[] getFl() {
		return fl;
	}

	public void setFl(String[] fl) {
		this.fl = fl;
	}

	public String[] getQf() {
		return qf;
	}

	public void setQf(String[] qf) {
		this.qf = qf;
	}

	public String[] getFq() {
		return fq;
	}

	public void setFq(String[] fq) {
		this.fq = fq;
	}

}

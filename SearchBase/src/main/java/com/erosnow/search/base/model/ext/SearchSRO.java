package com.erosnow.search.base.model.ext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SearchSRO implements Serializable {
	private long noOfResults;
	private boolean partialSearch;
	private List<SpellSuggestionSRO> spellSuggestions;
	private boolean spellCheckUsed;
	private List<Map<String, Collection<Object>>> fieldValueMap = new ArrayList<Map<String, Collection<Object>>>();

	public long getNoOfResults() {
		return noOfResults;
	}

	public void setNoOfResults(long noOfResults) {
		this.noOfResults = noOfResults;
	}

	public boolean isPartialSearch() {
		return partialSearch;
	}

	public void setPartialSearch(boolean partialSearch) {
		this.partialSearch = partialSearch;
	}

	public void setSpellSuggestions(List<SpellSuggestionSRO> spellSuggestions) {
		this.spellSuggestions = spellSuggestions;
	}

	public List<SpellSuggestionSRO> getSpellSuggestions() {
		return spellSuggestions;
	}

	public void setSpellCheckUsed(boolean spellCheckUsed) {
		this.spellCheckUsed = spellCheckUsed;
	}

	public boolean isSpellCheckUsed() {
		return spellCheckUsed;
	}

	public List<Map<String, Collection<Object>>> getFieldValueMap() {
		return fieldValueMap;
	}

	public void setFieldValueMap(List<Map<String, Collection<Object>>> fieldValueMap) {
		this.fieldValueMap = fieldValueMap;
	}

}

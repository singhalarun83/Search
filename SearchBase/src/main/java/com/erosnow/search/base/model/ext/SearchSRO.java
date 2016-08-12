package com.erosnow.search.base.model.ext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchSRO implements Serializable {
	private List<String> itemIds = new ArrayList<String>();
	private long noOfResults;
	private boolean partialSearch;
	private List<SpellSuggestionSRO> spellSuggestions;
	private boolean spellCheckUsed;

	public List<String> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<String> itemIds) {
		this.itemIds = itemIds;
	}

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
}

package com.erosnow.search.base.model.ext;

import java.io.Serializable;

public class SpellSuggestionSRO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8949816577379544410L;

	private String spellSuggestion;

	private Long hits;

	private Integer changeLength;

	public SpellSuggestionSRO() {
		super();
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Long getHits() {
		return hits;
	}

	public void setSpellSuggestion(String spellSuggestion) {
		this.spellSuggestion = spellSuggestion;
	}

	public String getSpellSuggestion() {
		return spellSuggestion;
	}

	public void setChangeLength(Integer changeLength) {
		this.changeLength = changeLength;
	}

	public Integer getChangeLength() {
		return changeLength;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((spellSuggestion == null) ? 0 : spellSuggestion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpellSuggestionSRO other = (SpellSuggestionSRO) obj;
		if (spellSuggestion == null) {
			if (other.spellSuggestion != null)
				return false;
		} else if (!spellSuggestion.equals(other.spellSuggestion))
			return false;
		return true;
	}
}

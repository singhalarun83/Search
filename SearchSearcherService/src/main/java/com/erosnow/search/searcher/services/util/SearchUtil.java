package com.erosnow.search.searcher.services.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Correction;

import com.erosnow.search.base.model.ext.SpellSuggestionSRO;
import com.erosnow.search.base.util.SortCategory;
import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.common.util.SolrFields;

public class SearchUtil {

	public static void setDefaultSort(SearchCriteria sc, SortCategory sortCategory) {
		if (sortCategory != null) {
			switch (sortCategory) {
			case RELEVANCY:
				sc.sort("score", SolrQuery.ORDER.desc);
				break;
			default:
				sc.sort(SolrFields.ID, ORDER.desc);
				break;
			}
		} else {
			sc.sort(SolrFields.ID, ORDER.desc);
		}
	}

	public static boolean isValidSearchTerm(String searchTerm) {
		return StringUtils.isNotEmpty(searchTerm) && searchTerm.split(" ").length > 1;
	}

	public static void spellCheckQueryBuilder(String searchTerm, boolean isSpellCheckEnabled, SearchCriteria sc) {
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		if (searchTerm != null && isSpellCheckEnabled) {
			String accuracy = "";
			if (searchTerm.length() > cache.getIntegerProperty(SearchPropertyEnum.SPELLCHECK_ACCURACY_LIMIT)) {
				accuracy = cache.getProperty(SearchPropertyEnum.SPELLCHECK_ACCURACY);
			} else {
				accuracy = cache.getProperty(SearchPropertyEnum.SPELLCHECK_LOWER_ACCURACY);
			}
			sc.spellCheck(searchTerm, Float.parseFloat(accuracy),
					cache.getBooleanProperty(SearchPropertyEnum.SPELLCHECK_ONLY_MORE_POPULAR),
					cache.getIntegerProperty(SearchPropertyEnum.SPELLCHECK_COUNT),
					cache.getIntegerProperty(SearchPropertyEnum.SPELLCHECK_COLLATION_COUNT),
					cache.getIntegerProperty(SearchPropertyEnum.SPELLCHECK_COLLATION_TRIES),
					cache.getIntegerProperty(SearchPropertyEnum.SPELLCHECK_MINIMUM_MATCH),
					cache.getIntegerProperty(SearchPropertyEnum.SPELLCHECK_MAX_COLLATE_DOCS),
					cache.getIntegerProperty(SearchPropertyEnum.SPELLCHECK_MAX_EVALUATIONS),
					cache.getProperty(SearchPropertyEnum.SPELLCHECK_DEFTYPE),
					cache.getProperty(SearchPropertyEnum.SPELLCHECK_MAX_RESULTS_FOR_SUGGEST),
					cache.getBooleanProperty(SearchPropertyEnum.SPELLCHECK_MAX_RESULTS_FOR_SUGGEST_ENABLE));
		}
	}

	public static List<SpellSuggestionSRO> getSpellCheckResponse(QueryResponse queryResponse, String inputKeyword) {
		SpellCheckResponse spellCheckResponse = queryResponse.getSpellCheckResponse();
		List<SpellSuggestionSRO> suggestionsSROs = new ArrayList<SpellSuggestionSRO>();
		if (spellCheckResponse != null && spellCheckResponse.getCollatedResults() != null) {
			for (Collation collation : spellCheckResponse.getCollatedResults()) {
				boolean flag = true;
				String keyword = collation.getCollationQueryString().replaceAll("[()]", "").replaceAll("( )+", " ");
				Integer minBreakLength = CacheManager.getInstance().getCache(SearchPropertyCache.class)
						.getIntegerProperty(SearchPropertyEnum.SPELLCHECK_MIN_BREAK_LENGTH);
				for (Correction missSpelling : collation.getMisspellingsAndCorrections()) {
					int spellWords = SearchUtil.countWords(missSpelling.getCorrection());
					if (spellWords > 2 || (spellWords == 2 && missSpelling.getOriginal().length() < minBreakLength)) {
						flag = false;
						break;
					}
				}
				if (flag && !keyword.trim().replaceAll("\\s", "").equals(inputKeyword.trim().replaceAll("\\s", ""))) {
					SpellSuggestionSRO spellSuggestionSRO = new SpellSuggestionSRO();
					spellSuggestionSRO.setSpellSuggestion(keyword.trim());
					spellSuggestionSRO.setHits(collation.getNumberOfHits());
					int changeLength = Math.abs(keyword.length() - inputKeyword.length());
					if (changeLength <= 1) {
						changeLength = 0;
					}
					spellSuggestionSRO.setChangeLength(changeLength);
					if (!suggestionsSROs.contains(spellSuggestionSRO)) {
						suggestionsSROs.add(spellSuggestionSRO);
					}
				}
			}

			Collections.sort(suggestionsSROs, new Comparator<SpellSuggestionSRO>() {
				@Override
				public int compare(SpellSuggestionSRO o1, SpellSuggestionSRO o2) {
					if (o1.getChangeLength() == o2.getChangeLength()) {
						if (CacheManager.getInstance().getCache(SearchPropertyCache.class)
								.getBooleanProperty(SearchPropertyEnum.SPELLCHECK_SORT_HITS)) {
							return o2.getHits().compareTo(o1.getHits());
						}
						return 0;
					} else {
						return o1.getChangeLength().compareTo(o2.getChangeLength());
					}
				}
			});
		}
		return suggestionsSROs;
	}

	public static int countWords(String s) {
		int counter = 0;
		boolean word = false;
		int endOfLine = s.length() - 1;
		for (int i = 0; i < s.length(); i++) {
			// if the char is letter, word = true.
			if ((Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i))) && i != endOfLine) {
				word = true;
				// if char isnt letter and there have been letters before (word
				// == true), counter goes up.
			} else if ((Character.isLetter(s.charAt(i)) || !Character.isDigit(s.charAt(i))) && word) {
				counter++;
				word = false;
				// last word of String, if it doesnt end with nonLetter it
				// wouldnt count without this.
			} else if ((Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i))) && i == endOfLine) {
				counter++;
			}
		}
		return counter;
	}
}

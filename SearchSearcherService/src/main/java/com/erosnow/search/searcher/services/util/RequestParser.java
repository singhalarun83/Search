package com.erosnow.search.searcher.services.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erosnow.search.base.model.ext.SearchRequest;
import com.erosnow.search.base.util.SortCategory;
import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;

public class RequestParser {

	private static final Logger LOG = LoggerFactory.getLogger(RequestParser.class);

	public static QueryBuilder parseRequest(SearchRequest request) {
		QueryBuilder builder = new QueryBuilder();
		builder.setSortBy(parseSorting(request.getSortBy()));
		builder.setNumber(parseMaxResult(request.getNumber()));
		builder.setDebug(request.isDebug()).setRequestClient(request.getRequestClient()).setQ(request.getQ())
				.setKeyword(request.getKeyword() != null ? request.getKeyword().trim() : request.getKeyword())
				.setSpellCheckEnabled(request.isSpellCheckEnabled()).setStart(request.getStart())
				.setSearchTerm(modifySearchTerm(request.getKeyword()))
				.setPartialSearchEnabled(request.isPartialSearchEnabled());
		return builder;
	}

	public static String modifySearchTerm(String searchTerm) {
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		if (StringUtils.isNotEmpty(searchTerm)) {
			searchTerm = searchTerm.toLowerCase().trim();
			searchTerm = limitSearchTermLength(searchTerm);
			if (searchTerm.split(" ").length < cache.getIntegerProperty(SearchPropertyEnum.SEARCH_KEYWORD_LIMIT)) {
				List<String> wordList = Arrays
						.asList(searchTerm.split(cache.getProperty(SearchPropertyEnum.SEARCH_KEYWORD_SPLIT_REGEX)));
				if ((null != wordList) && !wordList.isEmpty()) {
					searchTerm = StringUtils.join(wordList, " ");
				} else {
					LOG.warn("Search term was " + searchTerm + " and regex was "
							+ cache.getProperty(SearchPropertyEnum.SEARCH_KEYWORD_SPLIT_REGEX)
							+ " hence there was not splitting");
					searchTerm = "";
				}
			}
		}
		searchTerm = org.apache.commons.lang3.StringUtils.normalizeSpace(searchTerm);
		LOG.info("searchTerm modified by normalizing spaces: " + searchTerm);
		return searchTerm;
	}

	private static String limitSearchTermLength(String searchTerm) {
		String newSearchTerm = "";
		String[] split = searchTerm.split(" ");
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		int maxCharAllowed = cache.getIntegerProperty(SearchPropertyEnum.SEARCH_KEYWORD_CHAR_LIMIT);
		for (int i = 0; i < split.length; i++) {
			if (newSearchTerm.length() + split[i].length() > maxCharAllowed) {
				LOG.warn("Reached max character limit : Search term was " + searchTerm + " and new search term is : "
						+ newSearchTerm);
				break;
			}
			newSearchTerm += split[i] + " ";
		}
		return newSearchTerm;
	}

	public static SortCategory parseSorting(String sortBy) {
		return SortCategory.fromString(sortBy);
	}

	public static int parseMaxResult(int reqNumber) {
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		int maxSearchResults = cache.getIntegerProperty(SearchPropertyEnum.SEARCH_MAX_NUMBER_OF_RESULTS);
		int number = reqNumber;
		if (number > maxSearchResults) {
			number = maxSearchResults;
		}
		return number;
	}

}

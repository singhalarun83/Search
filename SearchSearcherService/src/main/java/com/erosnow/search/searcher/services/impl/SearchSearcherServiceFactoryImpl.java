package com.erosnow.search.searcher.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.erosnow.search.searcher.services.SearchSearcherServiceFactory;
import com.erosnow.search.searcher.services.searcher.SearchService;

@Component("searchSearcherServiceFactory")
public class SearchSearcherServiceFactoryImpl implements SearchSearcherServiceFactory {

	@Autowired
	private SearchService searchService;

	public SearchService getSearchService() {
		return searchService;
	}
}

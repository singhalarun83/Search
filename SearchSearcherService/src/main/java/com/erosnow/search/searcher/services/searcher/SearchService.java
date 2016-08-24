package com.erosnow.search.searcher.services.searcher;

import com.erosnow.search.base.model.ext.SearchSRO;
import com.erosnow.search.searcher.services.util.QueryBuilder;

public interface SearchService {

	public SearchSRO search(QueryBuilder builder) throws Exception;
}

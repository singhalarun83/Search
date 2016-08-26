package com.erosnow.search.searcher.services.searcher.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.erosnow.search.base.model.ext.SearchSRO;
import com.erosnow.search.base.model.ext.SpellSuggestionSRO;
import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.searcher.services.searcher.QueryService;
import com.erosnow.search.searcher.services.searcher.SearchService;
import com.erosnow.search.searcher.services.util.QueryBuilder;
import com.erosnow.search.searcher.services.util.SearchCriteria;
import com.erosnow.search.searcher.services.util.SearchUtil;

@Component("searchService")
public class SearchServiceImpl implements SearchService {

	private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Autowired
	private QueryService queryService;

	@Override
	public SearchSRO search(QueryBuilder builder) throws Exception {
		SearchCriteria sc = new SearchCriteria();
		SearchSRO searchSRO = new SearchSRO();
		setSpellCheck(builder, sc);
		setSearchCriteria(builder, sc);
		QueryResponse resp = executeQuery(sc);
		LOG.info("The number of results in standard response is " + resp.getResults().size());
		if (isSpellCheckEnabled(builder)) {
			resp = handleSpellCheck(builder, sc, resp, searchSRO);
		}
		if (isPartialSearchEnabled(builder)) {
			resp = handlePartialSearch(builder, sc, resp, searchSRO);
		}
		handleOutputFields(searchSRO, resp, builder);
		return searchSRO;
	}

	private void handleOutputFields(SearchSRO searchSRO, QueryResponse resp, QueryBuilder builder) {
		SolrDocumentList docList = resp.getResults();
		for (int i = 0; i < Math.min(docList.size(), builder.getNumber()); i++) {
			Map<String, Collection<Object>> fieldValuesMap = new HashMap<String, Collection<Object>>();
			for (String key : docList.get(i).keySet()) {
				fieldValuesMap.put(key, docList.get(i).getFieldValues(key));
			}
			searchSRO.getFieldValueMap().add(fieldValuesMap);
		}
		searchSRO.setNoOfResults(docList.getNumFound());
	}

	private QueryResponse handlePartialSearch(QueryBuilder builder, SearchCriteria sc, QueryResponse resp,
			SearchSRO searchSRO) throws Exception {
		if (resp.getResults().getNumFound() == 0 && isPartialSearchEnabled(builder)
				&& SearchUtil.isValidSearchTerm(builder.getSearchTerm())) {
			sc.mm(CacheManager.getInstance().getCache(SearchPropertyCache.class)
					.getProperty(SearchPropertyEnum.PARTIAL_SEARCH_MM));
			LOG.info("-----------------Partial Search Query : " + builder.getSearchTerm());
			resp = executeQuery(sc);
			searchSRO.setPartialSearch(true);
			searchSRO.setSpellCheckUsed(false);
		}
		return resp;
	}

	private QueryResponse handleSpellCheck(QueryBuilder builder, SearchCriteria sc, QueryResponse resp,
			SearchSRO searchSRO) throws Exception {
		List<SpellSuggestionSRO> spellSuggestionSROs = SearchUtil.getSpellCheckResponse(resp, builder.getKeyword());
		sc.removeSpellCheck();
		LOG.info("The number of suggestions in original query are " + spellSuggestionSROs.size());
		if (spellSuggestionSROs.size() > 0 && resp.getResults().size() <= 0) {
			String spellquery = spellSuggestionSROs.get(0).getSpellSuggestion();
			sc.query(spellquery);
			LOG.info("-----------------Spell Check Query : " + spellquery);
			resp = executeQuery(sc);
			searchSRO.setSpellCheckUsed(true);
		}
		searchSRO.setSpellSuggestions(spellSuggestionSROs);
		return resp;
	}

	private void setSpellCheck(QueryBuilder builder, SearchCriteria sc) {
		SearchUtil.spellCheckQueryBuilder(builder.getSearchTerm(), isSpellCheckEnabled(builder), sc);
	}

	private boolean isSpellCheckEnabled(QueryBuilder builder) {
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		return builder.isSpellCheckEnabled() && cache.getBooleanProperty(SearchPropertyEnum.SPELLCHECK_ENABLED);
	}

	private boolean isPartialSearchEnabled(QueryBuilder builder) {
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		return builder.isPartialSearchEnabled() && cache.getBooleanProperty(SearchPropertyEnum.PARTIAL_SEARCH_ENABLED);
	}

	private void setSearchCriteria(QueryBuilder builder, SearchCriteria sc) {
		sc.rows(builder.getNumber()).start(builder.getStart());
		SearchUtil.setDefaultSort(sc, builder.getSortBy());
		sc.query(builder.getSearchTerm());
		if (builder.getFl() != null)
			sc.fl(builder.getFl());
		if (builder.getQf() != null)
			sc.qf(builder.getQf());
		if (builder.getFq() != null)
			sc.addFilter(builder.getFq());
	}

	public QueryResponse executeQuery(SearchCriteria sc) throws Exception {
		return queryService.executeSolrQuery(sc.buildQuery());
	}
}

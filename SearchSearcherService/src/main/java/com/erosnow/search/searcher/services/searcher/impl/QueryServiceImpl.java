package com.erosnow.search.searcher.services.searcher.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.searcher.services.SolrServerConnectionFactory;
import com.erosnow.search.searcher.services.searcher.QueryService;

@Component("queryService")
public class QueryServiceImpl implements QueryService {

	private static final Logger LOG = LoggerFactory.getLogger(QueryServiceImpl.class);

	@Autowired
	private SolrServerConnectionFactory SearcherServerConnectionFactory;
	@Autowired
	private Environment environment;

	@Override
	public QueryResponse executeSolrQuery(SolrQuery q) throws Exception {
		try {
			String slaveUrl = environment.getRequiredProperty("solr.slave.url");
			HttpSolrClient server = SearcherServerConnectionFactory.getServerInstanceForSearching(slaveUrl);
			setQueryParam(q);
			LOG.info("Querying Solr: " + q.toString() + " on: " + server.toString());
			if (1 == CacheManager.getInstance().getCache(SearchPropertyCache.class)
					.getIntegerProperty(SearchPropertyEnum.SOLR_QUERY_HTTPCLIENT_POST)) {
				return server.query(q, SolrRequest.METHOD.POST);
			} else {
				return server.query(q);
			}
		} catch (Exception e) {
			LOG.error("error while fetching records!", e);
			throw new Exception("error fetching records from solr", e);
		}
	}

	private void setQueryParam(SolrQuery q) throws Exception {
		SearchPropertyCache propertiesCache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		if (q.toString().length() > propertiesCache
				.getIntegerProperty(SearchPropertyEnum.MAX_SOLR_QUERY_LENGTH_ALLOWED)) {
			throw new Exception("Query too big to handle.");
		}
		if (propertiesCache.getBooleanProperty(SearchPropertyEnum.ENABLE_SHARDS_TOLERANT)) {
			q.setParam("shards.tolerant", true);
			q.setParam("stats", true);
		}
		if (propertiesCache.getBooleanProperty(SearchPropertyEnum.ENABLE_SHARDS_INFO)) {
			q.setParam("shards.info", true);
		}
	}

}

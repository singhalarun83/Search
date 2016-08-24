package com.erosnow.search.searcher.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.searcher.services.SolrServerConnectionFactory;

@Service("SearcherServerConnectionFactory")
public class SolrServerConnectionFactoryImpl implements SolrServerConnectionFactory {

	private static Map<String, HttpSolrClient> solrSearcherServers = new HashMap<String, HttpSolrClient>();

	public HttpSolrClient getServerInstanceForSearching(String urlString) {
		HttpSolrClient instance = null;
		if (solrSearcherServers.containsKey(urlString)) {
			instance = solrSearcherServers.get(urlString);
		} else {
			synchronized (solrSearcherServers) {
				if (solrSearcherServers.containsKey(urlString)) {
					instance = solrSearcherServers.get(urlString);
				} else {
					instance = new HttpSolrClient.Builder(urlString).build();
					prepareSearcherSolrServer(instance);
					solrSearcherServers.put(urlString, instance);
				}
			}
		}
		return instance;
	}

	private static void prepareSearcherSolrServer(HttpSolrClient server) {
		SearchPropertyCache propertiesCache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		server.setSoTimeout(Integer.parseInt(propertiesCache.getProperty(SearchPropertyEnum.SEARCHER_SOLR_TIMEOUT)));
		server.setConnectionTimeout(
				Integer.parseInt(propertiesCache.getProperty(SearchPropertyEnum.SEARCHER_SOLR_CONNECTION_TIMEOUT)));
		server.setDefaultMaxConnectionsPerHost(
				Integer.parseInt(propertiesCache.getProperty(SearchPropertyEnum.SEARCHER_SOLR_CONNECTION_PER_HOST)));
		server.setMaxTotalConnections(
				Integer.parseInt(propertiesCache.getProperty(SearchPropertyEnum.SEARCHER_SOLR_MAX_TOTAL_CONNECTION)));
		server.setAllowCompression(true);
		server.setParser(new XMLResponseParser());
	}

}

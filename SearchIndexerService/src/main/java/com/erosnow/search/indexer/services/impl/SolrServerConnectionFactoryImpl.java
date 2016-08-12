package com.erosnow.search.indexer.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.SolrServerConnectionFactory;

@Service("IndexerServerConnectionFactory")
public class SolrServerConnectionFactoryImpl implements SolrServerConnectionFactory {

	private static Map<String, HttpSolrClient> solrIndexerServers = new HashMap<String, HttpSolrClient>();

	public HttpSolrClient getServerInstanceForIndexing(String urlString) {
		HttpSolrClient instance = null;
		if (solrIndexerServers.containsKey(urlString)) {
			instance = solrIndexerServers.get(urlString);
		} else {
			synchronized (solrIndexerServers) {
				if (solrIndexerServers.containsKey(urlString)) {
					instance = solrIndexerServers.get(urlString);
				} else {
					instance = new HttpSolrClient.Builder(urlString).build();
					prepareIndexerSolrServer(instance);
					solrIndexerServers.put(urlString, instance);
				}
			}
		}
		return instance;
	}

	private static void prepareIndexerSolrServer(HttpSolrClient server) {
		SearchPropertyCache propertiesCache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		server.setSoTimeout(Integer.parseInt(propertiesCache.getProperty(SearchPropertyEnum.INDEXER_SOLR_TIMEOUT)));
		server.setConnectionTimeout(
				Integer.parseInt(propertiesCache.getProperty(SearchPropertyEnum.INDEXER_SOLR_CONNECTION_TIMEOUT)));
		server.setDefaultMaxConnectionsPerHost(
				Integer.parseInt(propertiesCache.getProperty(SearchPropertyEnum.INDEXER_SOLR_CONNECTION_PER_HOST)));
		server.setMaxTotalConnections(
				Integer.parseInt(propertiesCache.getProperty(SearchPropertyEnum.INDEXER_SOLR_MAX_TOTAL_CONNECTION)));
		server.setAllowCompression(true);
		server.setParser(new XMLResponseParser());
	}

}

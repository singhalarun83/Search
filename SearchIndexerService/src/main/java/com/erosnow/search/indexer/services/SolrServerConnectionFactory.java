package com.erosnow.search.indexer.services;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

public interface SolrServerConnectionFactory {

	HttpSolrClient getServerInstanceForIndexing(String urlString);
}

package com.erosnow.search.searcher.services;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

public interface SolrServerConnectionFactory {

	HttpSolrClient getServerInstanceForSearching(String urlString);
}

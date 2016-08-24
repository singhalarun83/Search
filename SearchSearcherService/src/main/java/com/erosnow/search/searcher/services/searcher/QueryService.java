package com.erosnow.search.searcher.services.searcher;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

public interface QueryService {

	public QueryResponse executeSolrQuery(SolrQuery q) throws Exception;

}

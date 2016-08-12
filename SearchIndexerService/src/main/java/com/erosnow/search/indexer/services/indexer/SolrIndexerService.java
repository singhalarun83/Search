package com.erosnow.search.indexer.services.indexer;

import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

public interface SolrIndexerService {

	void index(List<SolrInputDocument> docs);

	public void index(List<SolrInputDocument> docs, HttpSolrClient server);

	public HttpSolrClient getSolrServerInstance(String masterUrl);

	public HttpSolrClient getSolrServerInstance();

}

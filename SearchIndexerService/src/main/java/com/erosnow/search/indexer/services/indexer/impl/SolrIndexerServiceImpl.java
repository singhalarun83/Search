package com.erosnow.search.indexer.services.indexer.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.log.DebugLogger;
import com.erosnow.search.common.log.IndexerLogger;
import com.erosnow.search.common.util.SolrFields;
import com.erosnow.search.indexer.services.SolrServerConnectionFactory;
import com.erosnow.search.indexer.services.indexer.SolrIndexerService;
import com.erosnow.search.indexer.services.producer.RequeueService;

@Service("solrIndexerService")
public class SolrIndexerServiceImpl implements SolrIndexerService {

	private static final Logger LOG = LoggerFactory.getLogger(SolrIndexerServiceImpl.class);

	@Autowired
	private RequeueService requeueService;

	@Autowired
	private Environment environment;

	@Autowired
	private SolrServerConnectionFactory solrServerConnectionFactory;

	@Override
	public void index(List<SolrInputDocument> docs) {
		HttpSolrClient server = getSolrServerInstance();
		index(docs, server);
	}

	@Override
	public HttpSolrClient getSolrServerInstance() {
		String masterUrl = environment.getRequiredProperty("solr.master.url");
		return getSolrServerInstance(masterUrl);
	}

	@Override
	public HttpSolrClient getSolrServerInstance(String masterUrl) {
		return solrServerConnectionFactory.getServerInstanceForIndexing(masterUrl);
	}

	private void printIndexerLogger(List<SolrInputDocument> docs, String msg) {
		for (SolrInputDocument doc : docs) {
			IndexerLogger.logError(doc.get(SolrFields.ID).getValue(), this.getClass(), msg);
		}
	}

	@Override
	public void index(List<SolrInputDocument> docs, HttpSolrClient server) {
		if (server == null) {
			LOG.error("Received null server. Can not index docs of size : " + docs.size());
			return;
		}
		List<SolrInputDocument> docsBackup = new LinkedList<SolrInputDocument>();
		for (SolrInputDocument doc : docs) {
			docsBackup.add(doc.deepCopy());
			DebugLogger.log("committing doc: " + doc);
		}
		List<SolrInputDocument> currentCommit = new LinkedList<SolrInputDocument>();
		List<String> currentDelete = new LinkedList<String>();
		for (SolrInputDocument doc : docs) {
			IndexerLogger.logIndex(doc.get(SolrFields.ID).getValue(), this.getClass());
			if (doc.containsKey(SolrFields.DELETE_THIS)) {
				currentDelete.add(String.valueOf(doc.get(SolrFields.ID).getValue()));
			} else {
				currentCommit.add(doc);
			}
		}
		UpdateResponse solrResponce = null;
		try {
			if (currentDelete.size() > 0) {
				LOG.info("Trying to delete by id for size : " + currentDelete.size());
				long start = System.currentTimeMillis();
				solrResponce = server.deleteById(currentDelete);
				if (solrResponce != null && solrResponce.getStatus() != 0) {
					IndexerLogger.logError(currentDelete, this.getClass(),
							"Solr Response status is " + solrResponce.getStatus());
					LOG.error("Delete operation failed on solr. solr responce status is " + solrResponce.getStatus()
							+ " for object Ids " + currentDelete);
				}
				LOG.info("Took time in deleting : " + String.valueOf(System.currentTimeMillis() - start));
			}
			if (currentCommit.size() > 0) {
				LOG.info("Trying to add document of size : " + currentCommit.size());
				long start = System.currentTimeMillis();
				solrResponce = server.add(currentCommit);
				// solrResponce = server.commit();
				if (solrResponce != null && solrResponce.getStatus() != 0) {
					printIndexerLogger(currentCommit, "Solr Response status is " + solrResponce.getStatus());
					LOG.error("Update operation failed on solr. Solr responce status is " + solrResponce.getStatus()
							+ " for solr Doc :" + currentCommit);
				}
				LOG.info("Took time in adding : " + String.valueOf(System.currentTimeMillis() - start));
			}
		} catch (Exception e) {
			printIndexerLogger(docs, e.getMessage());
			LOG.info("Error while committing " + docs.size(), e);
		}
	}

}

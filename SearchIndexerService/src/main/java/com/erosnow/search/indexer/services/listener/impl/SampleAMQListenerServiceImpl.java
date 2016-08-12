package com.erosnow.search.indexer.services.listener.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.log.DebugLogger;
import com.erosnow.search.common.util.Listener;
import com.erosnow.search.common.util.SolrFields;
import com.erosnow.search.indexer.services.dto.SolrDocumentBuilder;
import com.erosnow.search.indexer.services.indexer.SolrIndexerService;

@Service(Listener.SAMPLE_AMQ_LISTENER)
@Scope("prototype")
public class SampleAMQListenerServiceImpl extends AbstractAMQListenerServiceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(SampleAMQListenerServiceImpl.class);

	@Autowired
	private SolrIndexerService solrIndexerService;

	protected Set<Object> ids = new HashSet<Object>();

	public SampleAMQListenerServiceImpl() {
		super(Listener.SAMPLE_AMQ_LISTENER);
	}

	protected synchronized void accumulate(Object id) {
		ids.add(id);
	}

	protected synchronized List<Object> clear() {
		List<Object> idList = new ArrayList<Object>(ids);
		ids.clear();
		return idList;
	}

	@Override
	public synchronized Integer getAccumulatedSetSize() {
		return ids.size();
	}

	@Override
	public synchronized void index() {
		List<Object> idList = clear();
		DebugLogger.log("Going to index Objects  " + idList);
		solrIndexerService.index(sampleDocs());
	}

	private List<SolrInputDocument> sampleDocs() {
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		SolrDocumentBuilder sdb = SolrDocumentBuilder.newInstance().setId(SolrFields.ID, "1000");
		sdb.set("features", new ArrayList<String>(){{add("feature123");add("feature1234");add("feature3456");}});
		sdb.set("cat", new ArrayList<String>(){{add("cat1");add("cat2");add("cat3");}});
		sdb.set("store", new ArrayList<String>(){{add("store1");add("store2");add("store3");}});
		sdb.set("inStock", new ArrayList<Boolean>(){{add(true);}});
		sdb.set("popularity", new ArrayList<Long>(){{add(20l);add(30l);}});
		
		SolrDocumentBuilder sdb1 = SolrDocumentBuilder.newInstance().setId(SolrFields.ID, "2000");
		sdb1.set("deleteThis", true);
		
		SolrDocumentBuilder sdb2 = SolrDocumentBuilder.newInstance().setId(SolrFields.ID, "3000");
		sdb2.set("features", new ArrayList<String>(){{add("feature4");add("feature5");add("feature6");}});
		sdb2.set("cat", new ArrayList<String>(){{add("cat4");add("cat5");add("cat6");}});
		sdb2.set("store", new ArrayList<String>(){{add("store4");add("store5");add("store6");}});
		sdb2.set("inStock", new ArrayList<Boolean>(){{add(false);}});
		sdb2.set("popularity", new ArrayList<Long>(){{add(40l);add(50l);}});
		
		
		docs.add(sdb.build());
		docs.add(sdb1.build());
		docs.add(sdb2.build());
		return docs;
	}

	@SuppressWarnings({ "unchecked", "serial" })
	@Override
	protected void process(Serializable serialObj) {
		List<Object> objects = null;
		if (serialObj instanceof List) {
			objects = ((List<Object>) serialObj);
		} else {
			final Object obj = serialObj;
			objects = new ArrayList<Object>() {
				{
					add(obj);
				}
			};
		}
		logSizeMetric(objects.size());
		for (Object obj : objects) {
			logMessageHit(obj);
			accumulate(obj);
			if (config.getIndexSize() <= (getAccumulatedSetSize())) {
				callIndex();
			}
		}
	}
}

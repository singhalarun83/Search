package com.erosnow.search.indexer.services.dto;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erosnow.search.common.util.SolrFields;

public class SolrDocumentBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(SolrDocumentBuilder.class);

	private SolrInputDocument doc;

	private SolrDocumentBuilder() {
	}

	public static SolrDocumentBuilder newInstance() {
		SolrDocumentBuilder sdb = new SolrDocumentBuilder();
		sdb.doc = new SolrInputDocument();
		return sdb;
	}

	public SolrDocumentBuilder add(String field, Object value) {
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("add", value);
		this.doc.setField(field, updateMap);
		return this;
	}

	public SolrDocumentBuilder set(String field, Object value) {
		if (field.equals(SolrFields.ID))
			return this;
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("set", value);
		this.doc.setField(field, updateMap);
		return this;
	}

	public SolrDocumentBuilder setId(String field, Object value) {
		this.doc.setField(field, value);
		return this;
	}

	public SolrDocumentBuilder set(String field, Object value, float boost) {
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("set", value);
		this.doc.setField(field, updateMap, boost);
		return this;
	}

	public SolrDocumentBuilder remove(String field, Object value) {
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("remove", value);
		this.doc.setField(field, updateMap);
		return this;
	}

	public SolrDocumentBuilder remove(String field) {
		this.doc.removeField(field);
		return this;
	}

	public SolrInputDocument build() {
		if (this.doc.containsKey(SolrFields.ID)) {
			return doc;
		}
		LOG.error("Solr document must contain mandatory fields id");
		return null;
	}

}

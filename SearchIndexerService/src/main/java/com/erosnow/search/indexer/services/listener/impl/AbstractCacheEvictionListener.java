package com.erosnow.search.indexer.services.listener.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.BlockingCacheWithTTL.EvictionListener;
import com.erosnow.search.common.cache.impl.IndexerConfigurationCache;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.indexer.services.listener.ListenerService;

public abstract class AbstractCacheEvictionListener<K, V> implements EvictionListener<K, V>, ListenerService {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCacheEvictionListener.class);

	private Set<K> ids = new HashSet<K>();

	private Integer instanceNumber;

	private String consumerType;

	private ConsumerTypeConfiguration config;

	public void init(String consumerType, Integer instanceNumber) {
		this.consumerType = consumerType;
		this.config = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
				.getConsumerTypeConfigurationByType(consumerType);
		this.instanceNumber = instanceNumber;
	}

	@Override
	public void evict(K key, V value) {
		LOG.info("Evicted key : " + key + " value : " + value);
		this.accumulate(key);
		if (this.getIndexSize() <= (this.getAccumulatedSetSize())) {
			this.callIndex();
		}
	}

	private void callIndex() {
		long size = getAccumulatedSetSize();
		if (size > 0) {
			long startTime = Calendar.getInstance().getTimeInMillis();
			LOG.info("ThreadId: " + Thread.currentThread().getId() + " starting indexing for index size of: " + size
					+ " for: " + consumerType + " instance number: " + instanceNumber);
			index();
			LOG.info("ThreadId: " + Thread.currentThread().getId() + " total time to index " + size + " "
					+ (Calendar.getInstance().getTimeInMillis() - startTime) + " for: " + consumerType
					+ " instance number: " + instanceNumber);
		}
	}

	protected abstract void index();

	public void flushInstance() {
		LOG.info("Periodic Call to flush queue scheduler - START" + " for: " + consumerType + " instance number: "
				+ instanceNumber);
		if (CacheManager.getInstance().getCache(SearchPropertyCache.class) == null) {
			LOG.info("Do nothing & Return as cache not initialized");
			return;
		}
		if (isPeriodicFlushEnabled()) {
			callIndex();
		}
		LOG.info("Periodic Call to flush queue scheduler - END" + " for: " + consumerType + " instance number: "
				+ instanceNumber);
	}

	protected boolean isPeriodicFlushEnabled() {
		return config.getFlushInterval() > 0;
	}

	protected synchronized List<K> clear() {
		List<K> idList = new ArrayList<K>(ids);
		ids.removeAll(idList);
		return idList;
	}

	protected int getAccumulatedSetSize() {
		return ids.size();
	}

	protected int getIndexSize() {
		return config.getIndexSize();
	}

	protected synchronized void accumulate(K key) {
		ids.add(key);
	}

	public void setInstanceNumber(Integer instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}

	public void setConfig(ConsumerTypeConfiguration config) {
		this.config = config;
	}

}

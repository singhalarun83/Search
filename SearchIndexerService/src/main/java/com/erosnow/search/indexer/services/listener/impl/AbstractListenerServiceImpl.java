package com.erosnow.search.indexer.services.listener.impl;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.IndexerConfigurationCache;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.metrics.CustomMetricSet;

public abstract class AbstractListenerServiceImpl implements ApplicationContextAware {

	protected static Map<String, Integer> typeToCountMap = new ConcurrentHashMap<String, Integer>();

	protected Integer instanceNumber;

	protected String consumerType;

	protected ConsumerTypeConfiguration config;

	@Autowired
	private CustomMetricSet customMetricSet;

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private static final Logger LOG = LoggerFactory.getLogger(AbstractListenerServiceImpl.class);

	public abstract void index();

	public abstract Integer getAccumulatedSetSize();

	public AbstractListenerServiceImpl(String consumerType) {
		this.consumerType = consumerType;
		config = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
				.getConsumerTypeConfigurationByType(consumerType);
		synchronized (typeToCountMap) {
			Integer instanceCount = typeToCountMap.get(consumerType);
			if (instanceCount == null) {
				instanceCount = new Integer(0);
			}
			instanceCount++;
			typeToCountMap.put(consumerType, instanceCount);
			this.instanceNumber = instanceCount;
		}

	}

	public synchronized void flushInstance() {
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

	public String getConsumerType() {
		return consumerType;
	}

	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}

	protected boolean isPeriodicFlushEnabled() {
		return config.getFlushInterval() > 0;
	}

	protected void logSizeMetric(int size) {
		customMetricSet.updateQueuePacketSizeStats(consumerType, size);
	}

	protected void logQueueMetric() {
		customMetricSet.updateQueueStats(consumerType);
	}

	protected void logMessageHit(Object object) {
		logQueueMetric();
		LOG.info(config.getName() + " instance number: " + instanceNumber + " OnMessage hit Id - " + object
				+ " accumulatedMessages - " + getAccumulatedSetSize().toString());
	}

	public synchronized void callIndex() {
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
}

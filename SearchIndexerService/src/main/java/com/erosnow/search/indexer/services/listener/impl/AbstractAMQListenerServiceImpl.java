package com.erosnow.search.indexer.services.listener.impl;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.AggregatorConfigCache;
import com.erosnow.search.common.cache.impl.BlockingCacheWithTTL;
import com.erosnow.search.indexer.services.listener.AMQListenerService;

public abstract class AbstractAMQListenerServiceImpl extends AbstractListenerServiceImpl implements AMQListenerService {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractAMQListenerServiceImpl.class);

	protected static BlockingCacheWithTTL<Object> lruCache;
	private AbstractCacheEvictionListener<Object, Long> evictListener;

	public AbstractAMQListenerServiceImpl(String consumerType) {
		super(consumerType);
	}

	@SuppressWarnings("unchecked")
	protected void setupCache(int maxSize, int ttl, String evictBean) {
		lruCache = CacheManager.getInstance().getCache(AggregatorConfigCache.class).getAggregatorConfig(consumerType);
		if (lruCache == null) {
			synchronized (this.getClass()) {
				if (CacheManager.getInstance().getCache(AggregatorConfigCache.class)
						.getAggregatorConfig(consumerType) == null) {
					CacheManager.getInstance().getCache(AggregatorConfigCache.class).addAggregatorConfig(consumerType,
							new BlockingCacheWithTTL<Object>(maxSize, ttl, this.consumerType));
				}
				lruCache = CacheManager.getInstance().getCache(AggregatorConfigCache.class)
						.getAggregatorConfig(consumerType);
			}
		}
		if (lruCache != null && lruCache.getEvictionListener() == null) {
			synchronized (this.getClass()) {
				if (lruCache.getEvictionListener() == null) {
					evictListener = (AbstractCacheEvictionListener<Object, Long>) applicationContext.getBean(evictBean);
					evictListener.setConfig(config);
					evictListener.setConsumerType(consumerType);
					evictListener.setInstanceNumber(instanceNumber);
					lruCache.setEvictionListener(evictListener);
				}
			}
		}
	}

	protected abstract void process(Serializable object);

	@Override
	public void onMessage(Message message) {
		try {
			process(((ObjectMessage) message).getObject());
		} catch (JMSException e) {
			LOG.error("Exception on message: ", e);
		}
	}
}

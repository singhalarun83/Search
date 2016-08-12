package com.erosnow.search.indexer.services.consumer.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.AggregatorConfigCache;
import com.erosnow.search.common.cache.impl.BlockingCacheWithTTL.EvictionListener;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.consumer.ConsumerSpawnService;
import com.erosnow.search.indexer.services.listener.ListenerService;

@Service("evictListenerSpawnService")
public class EvictListenerSpawnServiceImpl implements ConsumerSpawnService {

	private static final Logger LOG = LoggerFactory.getLogger(EvictListenerSpawnServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public void flushAll() {
		int poolSize = CacheManager.getInstance().getCache(SearchPropertyCache.class)
				.getIntegerProperty(SearchPropertyEnum.FLUSH_CORE_THREAD_POOL_SIZE);
		ExecutorService executorService = new ThreadPoolExecutor(poolSize, 1000, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		LOG.info("Flushing evictListener - START with core pool size: {}", poolSize);
		List<EvictionListener<String, Long>> allEvictListeners = CacheManager.getInstance()
				.getCache(AggregatorConfigCache.class).getAllEvictListeners();
		for (final EvictionListener<String, Long> listenerService : allEvictListeners) {
			if (listenerService instanceof ListenerService) {
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						((ListenerService) listenerService).flushInstance();
					}
				});
			}
		}
		executorService.shutdown();
		try {
			LOG.info("Waiting for Flushing Threads to Shutdown");
			executorService.awaitTermination(1, TimeUnit.HOURS);
			LOG.info("Flush Threads Shutdown Successfully");

		} catch (InterruptedException e) {
			LOG.error("exception while shutting down Flush threads", e);
		}
		LOG.info("Flushing all instances - END");
	}

	@Override
	public void spawnListenersByType(String type, int instances, String groupId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getConsumerStatusByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getCompleteConsumerStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConsumerStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregisterInstances(String consumerType, String consumerGroupId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterInstances(String consumerType) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requeue(String consumerType, Serializable object) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

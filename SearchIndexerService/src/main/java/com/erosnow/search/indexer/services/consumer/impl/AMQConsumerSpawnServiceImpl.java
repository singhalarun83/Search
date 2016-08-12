package com.erosnow.search.indexer.services.consumer.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.erosnow.search.common.activemq.ActiveMQManager;
import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.IndexerConfigurationCache;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.util.ActiveMQConstants;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.listener.AMQListenerService;
import com.erosnow.search.indexer.services.listener.ListenerService;

@Component("aMQConsumerSpawnService")
public class AMQConsumerSpawnServiceImpl extends AbstractConsumerSpawnService {

	private static final Logger LOG = LoggerFactory.getLogger(AMQConsumerSpawnServiceImpl.class);

	private Map<String, List<Long>> typeToTokensMap = new ConcurrentHashMap<String, List<Long>>();
	private Map<Long, ListenerService> tokenToListenerMap = new ConcurrentHashMap<Long, ListenerService>();
	private Map<String, Long> typeToTokenProducerMap = new ConcurrentHashMap<String, Long>();

	@Autowired
	ActiveMQManager activeMQManager;

	@Override
	public void spawnListenersByType(String type, int instances, String groupId) throws Exception {
		List<Long> tokens = typeToTokensMap.get(type);
		if (tokens == null) {
			tokens = new ArrayList<Long>();
		}
		LOG.info("Spawning " + instances + " instances of service of Type:" + type);
		while (instances > 0) {
			ConsumerTypeConfiguration config = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
					.getConsumerTypeConfigurationByType(type);
			AMQListenerService listener = (AMQListenerService) applicationContext.getBean(config.getName());
			Long token = activeMQManager.registerSubscriber(config.getQueueName(), config.getQueueUrl(),
					ActiveMQConstants.DEFAULT_USERNAME, ActiveMQConstants.DEFAULT_PASSWORD, listener);
			tokens.add(token);
			tokenToListenerMap.put(token, listener);
			instances--;

		}
		typeToTokensMap.put(type, tokens);
	}

	private List<Long> getListenersByType(String type) {
		List<Long> tokens = typeToTokensMap.get(type);
		if (tokens == null) {
			tokens = new ArrayList<Long>();
		}
		return tokens;
	}

	private void removeListenersByType(String type) {
		List<Long> tokens = typeToTokensMap.get(type);
		if (tokens != null) {
			tokens.clear();
		}
	}

	private void removeListenerByToken(Long token) {
		ListenerService listenerService = tokenToListenerMap.get(token);
		listenerService.flushInstance();
		tokenToListenerMap.remove(token);
	}

	@Override
	public void flushAll() {
		int poolSize = CacheManager.getInstance().getCache(SearchPropertyCache.class)
				.getIntegerProperty(SearchPropertyEnum.FLUSH_CORE_THREAD_POOL_SIZE);
		ExecutorService executorService = new ThreadPoolExecutor(poolSize, 1000, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		LOG.info("Flushing {} instances - START with core pool size: {}", tokenToListenerMap.values().size(), poolSize);
		for (final ListenerService listenerService : tokenToListenerMap.values()) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					listenerService.flushInstance();
				}
			});
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
	public Map<String, Integer> getCompleteConsumerStatus() {
		Map<String, Integer> consumerTypeToCountMap = new HashMap<String, Integer>();
		for (Entry<String, List<Long>> entry : typeToTokensMap.entrySet()) {
			consumerTypeToCountMap.put(entry.getKey(), entry.getValue().size());
		}
		return consumerTypeToCountMap;
	}

	@Override
	public String getConsumerStatus() {
		StringBuilder sb = new StringBuilder();
		boolean isAppended = false;
		for (Entry<String, List<Long>> entry : typeToTokensMap.entrySet()) {
			if (!isAppended) {
				sb.append("No of Consumers");
				isAppended = true;
			}
			sb.append(" for type ").append(entry.getKey()).append(" is: ").append(entry.getValue().size()).append(",");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@Override
	public String getConsumerStatusByType(String type) {
		StringBuilder sb = new StringBuilder();
		List<Long> tokens = typeToTokensMap.get(type);
		if (tokens != null && !tokens.isEmpty()) {
			sb.append("No of Consumers for type ").append(type).append(" is: ").append(tokens.size());
		} else {
			sb.append("No consumers found for type ").append(type);
		}
		return sb.toString();
	}

	@Override
	public void unregisterInstances(String consumerType) throws Exception {
		unregisterInstances(consumerType, null);
	}

	@Override
	public void unregisterInstances(String consumerType, String consumerGroupId) throws Exception {
		for (Long token : getListenersByType(consumerType)) {
			activeMQManager.unregisterSubscriber(token);
			removeListenerByToken(token);
		}
		removeListenersByType(consumerType);
	}

	@Override
	public void requeue(String consumerType, Serializable object) throws Exception {
		Long token = typeToTokenProducerMap.get(consumerType);
		if (token == null) {
			ConsumerTypeConfiguration config = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
					.getConsumerTypeConfigurationByType(consumerType);
			token = activeMQManager.registerPublisher(config.getQueueName(), config.getQueueUrl(),
					ActiveMQConstants.DEFAULT_USERNAME, ActiveMQConstants.DEFAULT_PASSWORD);
			typeToTokenProducerMap.put(consumerType, token);
			LOG.info("Producer registered successfully {}", config.toString());
		}
		activeMQManager.publish(token, object);
	}
}

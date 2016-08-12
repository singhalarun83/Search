package com.erosnow.search.indexer.services.consumer.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.IndexerConfigurationCache;
import com.erosnow.search.common.cache.impl.ProducerConsumerConfigCache;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.dto.KafkaPushListDTO;
import com.erosnow.search.indexer.services.listener.KafkaListenerService;
import com.erosnow.search.indexer.services.listener.ListenerService;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

@Component("kafkaConsumerSpawnService")
public class KafkaConsumerSpawnServiceImpl extends AbstractConsumerSpawnService {

	// list of tokens per topic per consumerGroup
	private Map<String, Map<String, List<String>>> typeToTokensMap = new HashMap<String, Map<String, List<String>>>();

	// Streams per topic per consumerGroup
	private Map<String, Map<String, List<KafkaStream<byte[], byte[]>>>> topicToStreamMap = new HashMap<String, Map<String, List<KafkaStream<byte[], byte[]>>>>();

	// Listener for each token
	private Map<String, ListenerService> tokenToListenerMap = new HashMap<String, ListenerService>();

	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerSpawnServiceImpl.class);

	public void spawnListenersByType(String type, int instances, String groupId) throws JMSException {
		Map<String, List<String>> map = typeToTokensMap.get(type);
		map = (map == null) ? new HashMap<String, List<String>>() : map;
		List<String> tokens = map.get(groupId);
		if (tokens != null && tokens.size() > 0) {
			LOG.error(" Consumers for type : " + type + " already exist. Not Attempting to spawn more instances.");
			throw new RuntimeException(
					" Consumers for type : " + type + " already exist. Not Attempting to spawn more instances.");
		}
		if (tokens == null) {
			tokens = new ArrayList<String>();
			topicToStreamMap.put(type, new HashMap<String, List<KafkaStream<byte[], byte[]>>>());
		}
		LOG.info("Spawning " + instances + " instances of service of Type:" + type);
		ProducerConsumerConfigCache cache = CacheManager.getInstance().getCache(ProducerConsumerConfigCache.class);
		ConsumerTypeConfiguration config = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
				.getConsumerTypeConfigurationByType(type);
		String queueName = config.getQueueName();
		LOG.info("Kafka topic is ::" + queueName + "and group id is ::" + groupId);
		ConsumerConfig consumerConfig = cache.getConsumerConfig(queueName, groupId);
		LOG.info("consumer config is ::" + consumerConfig);
		if (consumerConfig != null) {
			Map<String, Integer> localCountMap = new HashMap<String, Integer>();
			localCountMap.put(queueName, new Integer(instances));
			ConsumerConnector consumerObj = Consumer.createJavaConsumerConnector(consumerConfig);
			List<KafkaStream<byte[], byte[]>> streams = consumerObj.createMessageStreams(localCountMap).get(queueName);
			long tokenNum = 1;
			for (KafkaStream<byte[], byte[]> stream : streams) {
				KafkaListenerService listener = (KafkaListenerService) applicationContext.getBean(config.getName());
				listener.setStream(stream);
				listener.setGroupId(groupId);
				new Thread(listener).start();
				// Generate unique tokens//
				String token = type + "_" + groupId + "_" + tokenNum;
				tokens.add(token);
				// Update listeners map
				tokenToListenerMap.put(token, listener);
				tokenNum++;
			}
			// save consumer obj for further reference.
			cache.addConsumer(queueName, groupId, consumerObj);
			// Update token references.
			map.put(groupId, tokens);
			typeToTokensMap.put(type, map);
			// Update stream references.
			topicToStreamMap.get(type).put(groupId, streams);
		} else {
			LOG.error("Error while getting counumer configurations for type : " + type + " Please check configs.");
			throw new RuntimeException("Error while getting counumer configurations for type : " + type);
		}
	}

	@Override
	public String getConsumerStatus() {
		StringBuilder sb = new StringBuilder();
		boolean isAppended = false;
		for (String type : typeToTokensMap.keySet()) {
			for (Entry<String, List<String>> map : typeToTokensMap.get(type).entrySet()) {
				if (!isAppended) {
					sb.append("No of Consumers");
					isAppended = true;
				}
				sb.append(" for type ").append(type).append(" and group is : ").append(map.getKey()).append(" is: ")
						.append(map.getValue().size()).append(",");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@Override
	public String getConsumerStatusByType(String type) {
		StringBuilder sb = new StringBuilder();
		Map<String, List<String>> map = typeToTokensMap.get(type);
		List<String> tokens = new ArrayList<String>();
		for (String groupId : map.keySet()) {
			tokens.addAll(map.get(groupId));
		}
		if (tokens != null && !tokens.isEmpty()) {
			sb.append("No of Consumers for type ").append(type).append(" is: ").append(tokens.size());
		} else {
			sb.append("No consumers found for type ").append(type);
		}
		return sb.toString();
	}

	@Override
	public void flushAll() {
		int poolSize = CacheManager.getInstance().getCache(SearchPropertyCache.class)
				.getIntegerProperty(SearchPropertyEnum.FLUSH_CORE_THREAD_POOL_SIZE);
		ExecutorService executorService = new ThreadPoolExecutor(poolSize, 1000, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		LOG.info("Flushing {} instances - START with core pool size: {}", tokenToListenerMap.values().size(), poolSize);
		for (String type : tokenToListenerMap.keySet()) {
			final ListenerService listenerService = tokenToListenerMap.get(type);
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
	public void unregisterInstances(String consumerType) throws Exception {
		Map<String, List<String>> consumerTypeMap = typeToTokensMap.get(consumerType);
		if (consumerTypeMap == null) {
			LOG.error("Can not unregister. No consumers for type : " + consumerType);
			return;
		}
		for (String groupId : consumerTypeMap.keySet()) {
			unregisterInstances(consumerType, groupId);
		}
	}

	@Override
	public void unregisterInstances(String consumerType, String groupId) throws Exception {
		ProducerConsumerConfigCache cache = CacheManager.getInstance().getCache(ProducerConsumerConfigCache.class);
		String queueName = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
				.getConsumerTypeConfigurationByType(consumerType).getQueueName();
		ConsumerConnector consumer = cache.getConsumer(queueName, groupId);
		if (consumer != null) {
			try {
				consumer.shutdown();
				// remove token references.
				removeTokenByType(consumerType, groupId);
				// remove stream
				topicToStreamMap.get(consumerType).remove(groupId);
			} catch (Exception e) {
				LOG.error("Caught exception while unregistering : " + e.getMessage(), e);
			}
		}
	}

	public void removeTokenByType(String consumerType, String groupId) {
		for (String token : typeToTokensMap.get(consumerType).get(groupId)) {
			ListenerService listenerService = tokenToListenerMap.get(token);
			listenerService.flushInstance();
			tokenToListenerMap.remove(token);
		}
		Map<String, List<String>> map = typeToTokensMap.get(consumerType);
		map.remove(groupId);
		typeToTokensMap.put(consumerType, map);
	}

	@Override
	public Map<String, Integer> getCompleteConsumerStatus() {
		Map<String, Integer> consumerTypeToCountMap = new HashMap<String, Integer>();
		int count = 0;
		for (String type : typeToTokensMap.keySet()) {
			count = 0;
			for (String groupId : typeToTokensMap.get(type).keySet()) {
				count = +typeToTokensMap.get(type).get(groupId).size();
			}
			consumerTypeToCountMap.put(type, count);
		}
		return consumerTypeToCountMap;
	}

	@Override
	public void requeue(String consumerType, Serializable object) throws Exception {
		ConsumerTypeConfiguration config = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
				.getConsumerTypeConfigurationByType(consumerType);
		SearchPropertyCache propertyCache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		ProducerConsumerConfigCache producerCache = CacheManager.getInstance()
				.getCache(ProducerConsumerConfigCache.class);
		String queueName = config.getQueueName();
		Producer<String, Serializable> producer = producerCache.getProducer(queueName);
		List<Serializable> partitionData = null;
		if (propertyCache.getBooleanProperty(SearchPropertyEnum.KAFKA_PARTITION_BY_ACCUMULATION)) {
			Integer numOfPartition = producerCache.getNumOfPartitionsForType(config.getQueueName());
			numOfPartition = numOfPartition == null
					? propertyCache.getIntegerProperty(SearchPropertyEnum.NUMBER_OF_PARTITION) : numOfPartition;
			partitionData = partition(object, numOfPartition.intValue());
		} else {
			partitionData = partition(object);
		}

		for (Serializable dataToSend : partitionData) {
			KafkaPushListDTO dto = (KafkaPushListDTO) dataToSend;
			producer.send(new KeyedMessage<String, Serializable>(queueName, String.valueOf(dto.getKey()), dataToSend));
		}
	}

}

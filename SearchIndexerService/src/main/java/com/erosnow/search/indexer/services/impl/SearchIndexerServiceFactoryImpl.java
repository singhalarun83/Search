package com.erosnow.search.indexer.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.IndexerConfigurationCache;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.indexer.services.SearchIndexerServiceFactory;
import com.erosnow.search.indexer.services.consumer.ConsumerSpawnService;
import com.erosnow.search.indexer.services.util.QueueType;

@Component("searchIndexerServiceFactory")
public class SearchIndexerServiceFactoryImpl implements SearchIndexerServiceFactory {

	@Autowired
	@Qualifier("aMQConsumerSpawnService")
	private ConsumerSpawnService aMQConsumerSpawnService;

	@Autowired
	@Qualifier("kafkaConsumerSpawnService")
	private ConsumerSpawnService kafkaConsumerSpawnService;

	@Autowired
	@Qualifier("evictListenerSpawnService")
	private ConsumerSpawnService evictListenerSpawnService;

	public ConsumerSpawnService getConsumerSpawnService(String type) {
		ConsumerTypeConfiguration config = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
				.getConsumerTypeConfigurationByType(type);
		if (QueueType.KAFKA.toString().equalsIgnoreCase(config.getQueueType())) {
			return kafkaConsumerSpawnService;
		}
		return aMQConsumerSpawnService;
	}

	public List<ConsumerSpawnService> getAllConsumerSpawnServices() {
		List<ConsumerSpawnService> services = new ArrayList<ConsumerSpawnService>();
		services.add(kafkaConsumerSpawnService);
		services.add(aMQConsumerSpawnService);
		return services;
	}

	public List<ConsumerSpawnService> getAllFlushableServices() {
		List<ConsumerSpawnService> services = getAllConsumerSpawnServices();
		services.add(evictListenerSpawnService);
		return services;
	}
}

package com.erosnow.search.indexer.services;

import java.util.List;

import com.erosnow.search.indexer.services.consumer.ConsumerSpawnService;

public interface SearchIndexerServiceFactory {

	ConsumerSpawnService getConsumerSpawnService(String type);

	public List<ConsumerSpawnService> getAllConsumerSpawnServices();

	public List<ConsumerSpawnService> getAllFlushableServices();
}

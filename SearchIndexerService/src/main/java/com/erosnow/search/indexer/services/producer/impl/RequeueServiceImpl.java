package com.erosnow.search.indexer.services.producer.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erosnow.search.indexer.services.SearchIndexerServiceFactory;
import com.erosnow.search.indexer.services.consumer.ConsumerSpawnService;
import com.erosnow.search.indexer.services.producer.RequeueService;

@Service("requeueService")
public class RequeueServiceImpl implements RequeueService {

	@Autowired
	SearchIndexerServiceFactory searchIndexerServiceFactory;

	Map<String, Long> typeToTokenMap = new ConcurrentHashMap<String, Long>();

	@Override
	public void requeue(String consumerType, Serializable object) throws Exception {
		ConsumerSpawnService spawnService = searchIndexerServiceFactory.getConsumerSpawnService(consumerType);
		spawnService.requeue(consumerType, object);
	}

}

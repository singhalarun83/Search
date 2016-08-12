package com.erosnow.search.indexer.services.consumer;

import java.io.Serializable;
import java.util.Map;

public interface ConsumerSpawnService extends ConsumerFlushService {

	void spawnListenersByType(String type, int instances, String groupId) throws Exception;

	String getConsumerStatusByType(String type);

	Map<String, Integer> getCompleteConsumerStatus();

	String getConsumerStatus();

	void unregisterInstances(String consumerType, String consumerGroupId) throws Exception;

	void unregisterInstances(String consumerType) throws Exception;

	void requeue(String consumerType, Serializable object) throws Exception;
}

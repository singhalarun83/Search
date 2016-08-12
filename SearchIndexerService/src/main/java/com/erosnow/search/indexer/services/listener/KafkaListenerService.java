package com.erosnow.search.indexer.services.listener;

import kafka.consumer.KafkaStream;

public interface KafkaListenerService extends ListenerService, Runnable{

	void setGroupId(String groupId);

	void setStream(KafkaStream<byte[], byte[]> stream);
}

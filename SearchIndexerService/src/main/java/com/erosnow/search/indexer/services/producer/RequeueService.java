package com.erosnow.search.indexer.services.producer;

import java.io.Serializable;

public interface RequeueService {

	void requeue(String consumerType, Serializable ids) throws Exception;
}

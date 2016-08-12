package com.erosnow.search.indexer.services.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class KafkaPartitioner implements Partitioner {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaPartitioner.class);

	public KafkaPartitioner(VerifiableProperties props) {
	}

	@Override
	public int partition(Object key, int numOfPartition) {
		int partition = 0;
		if (key != null && key != "") {
			Long id = Long.valueOf(String.valueOf(key));
			if (id > 0) {
				partition = (int) (id % numOfPartition);
			}
		}
		LOG.info("Partitioning data with key : " + key + " number of partitions value : " + numOfPartition
				+ ". partition  value is :" + partition);
		return partition;
	}

}

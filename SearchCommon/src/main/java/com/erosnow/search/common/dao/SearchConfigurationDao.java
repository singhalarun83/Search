package com.erosnow.search.common.dao;

import java.util.List;

import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.entity.IndexerConfiguration;
import com.erosnow.search.common.entity.ProducerConsumerConfiguration;
import com.erosnow.search.common.entity.SearchProperty;

public interface SearchConfigurationDao {

	List<SearchProperty> loadAllSearchProperties();

	List<IndexerConfiguration> loadAllIndexerConfigurations();

	List<ConsumerTypeConfiguration> loadAllConsumerTypeConfigurations();

	List<ProducerConsumerConfiguration> loadAllProducerConsumerConfigurations();

}

package com.erosnow.search.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erosnow.search.common.dao.SearchConfigurationDao;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.entity.IndexerConfiguration;
import com.erosnow.search.common.entity.ProducerConsumerConfiguration;
import com.erosnow.search.common.entity.SearchProperty;
import com.erosnow.search.common.service.SearchConfigurationService;

@Service("searchConfigurationService")
@Transactional
public class SearchConfigurationServiceImpl implements SearchConfigurationService {

	@Autowired
	private SearchConfigurationDao dao;

	public List<SearchProperty> loadAllSearchProperties() {
		return dao.loadAllSearchProperties();
	}

	public List<IndexerConfiguration> loadAllIndexerConfigurations() {
		return dao.loadAllIndexerConfigurations();
	}

	public List<ConsumerTypeConfiguration> loadAllConsumerTypeConfigurations() {
		return dao.loadAllConsumerTypeConfigurations();
	}

	public List<ProducerConsumerConfiguration> loadAllProducerConsumerConfigurations() {
		return dao.loadAllProducerConsumerConfigurations();
	}

}

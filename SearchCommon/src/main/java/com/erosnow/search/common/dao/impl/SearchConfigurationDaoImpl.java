package com.erosnow.search.common.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.erosnow.search.common.dao.SearchConfigurationDao;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.entity.IndexerConfiguration;
import com.erosnow.search.common.entity.ProducerConsumerConfiguration;
import com.erosnow.search.common.entity.SearchProperty;

@Repository("searchConfigurationDao")
public class SearchConfigurationDaoImpl extends AbstractDaoImpl implements SearchConfigurationDao {

	@SuppressWarnings("unchecked")
	public List<SearchProperty> loadAllSearchProperties() {
		Criteria criteria = getSession().createCriteria(SearchProperty.class);
		return (List<SearchProperty>) criteria.list();
	}

	public List<IndexerConfiguration> loadAllIndexerConfigurations() {
		Criteria criteria = getSession().createCriteria(IndexerConfiguration.class);
		return (List<IndexerConfiguration>) criteria.list();
	}

	public List<ConsumerTypeConfiguration> loadAllConsumerTypeConfigurations() {
		Criteria criteria = getSession().createCriteria(ConsumerTypeConfiguration.class);
		return (List<ConsumerTypeConfiguration>) criteria.list();
	}

	public List<ProducerConsumerConfiguration> loadAllProducerConsumerConfigurations() {
		Criteria criteria = getSession().createCriteria(ProducerConsumerConfiguration.class)
				.addOrder(Order.asc("topicName")).addOrder(Order.asc("config"));
		return (List<ProducerConsumerConfiguration>) criteria.list();
	}
}

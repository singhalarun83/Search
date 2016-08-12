package com.erosnow.search.common.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erosnow.search.common.cache.Cache;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.entity.IndexerConfiguration;

@Cache(name = "indexerConfigurationCache")
public class IndexerConfigurationCache {

    private static final Logger                    LOG               = LoggerFactory.getLogger(IndexerConfigurationCache.class);

    private List<ConsumerTypeConfiguration>        consumerTypes     = new ArrayList<ConsumerTypeConfiguration>();
    private List<IndexerConfiguration>             ics               = new ArrayList<IndexerConfiguration>();
    private Map<String, ConsumerTypeConfiguration> typeToConsumerMap = new HashMap<String, ConsumerTypeConfiguration>();

    public void addConsumerTypeConfiguration(ConsumerTypeConfiguration consumerType) {
        consumerTypes.add(consumerType);
        typeToConsumerMap.put(consumerType.getName(), consumerType);
    }

    public void addIndexerConfiguration(IndexerConfiguration ic) {
        ics.add(ic);
    }

    public List<IndexerConfiguration> getAllICs() {
        return ics;
    }

    public List<ConsumerTypeConfiguration> getAllConsumerTypes() {
        return consumerTypes;
    }

    public ConsumerTypeConfiguration getConsumerTypeConfigurationByType(String type) {
        return typeToConsumerMap.get(type);
    }

	@Override
	public String toString() {
		return "IndexerConfigurationCache [consumerTypes=" + consumerTypes + ", ics=" + ics + ", typeToConsumerMap="
				+ typeToConsumerMap + "]";
	}

}

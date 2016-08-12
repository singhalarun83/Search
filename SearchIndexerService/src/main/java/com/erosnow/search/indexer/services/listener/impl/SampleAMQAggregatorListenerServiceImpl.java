package com.erosnow.search.indexer.services.listener.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.log.DebugLogger;
import com.erosnow.search.common.util.Listener;
import com.erosnow.search.common.util.SearchPropertyEnum;

@Service(Listener.SAMPLE_AMQ_AGGREGATOR_LISTENER)
@Scope("prototype")
public class SampleAMQAggregatorListenerServiceImpl extends AbstractAMQListenerServiceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(SampleAMQAggregatorListenerServiceImpl.class);

	protected Set<Object> ids = new HashSet<Object>();

	public SampleAMQAggregatorListenerServiceImpl() {
		super(Listener.SAMPLE_AMQ_AGGREGATOR_LISTENER);
	}
	
	@PostConstruct
	public void init(){
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		setupCache(cache.getIntegerProperty(SearchPropertyEnum.AGGR_SAMPLE_CACHE_MAX_SIZE),
				cache.getIntegerProperty(SearchPropertyEnum.AGGR_SAMPLE_CACHE_TTL), "sampleAMQCacheEvictListener");
	}

	protected synchronized void accumulate(Object id) {
		ids.add(id);
	}

	protected synchronized List<Object> clear() {
		List<Object> idList = new ArrayList<Object>(ids);
		ids.clear();
		return idList;
	}

	@Override
	public synchronized Integer getAccumulatedSetSize() {
		return ids.size();
	}

	@Override
	public synchronized void index() {
		List<Object> idList = clear();
		DebugLogger.log("Going to index Objects in aggr " + idList);
		lruCache.put(idList);
		// index service call here
	}

	@SuppressWarnings({ "unchecked", "serial" })
	@Override
	protected void process(Serializable serialObj) {
		List<Object> objects = null;
		if (serialObj instanceof List) {
			objects = ((List<Object>) serialObj);
		} else {
			final Object obj = serialObj;
			objects = new ArrayList<Object>() {
				{
					add(obj);
				}
			};
		}
		logSizeMetric(objects.size());
		for (Object obj : objects) {
			logMessageHit(obj);
			accumulate(obj);
			if (config.getIndexSize() <= (getAccumulatedSetSize())) {
				callIndex();
			}
		}
	}
}

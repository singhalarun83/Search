package com.erosnow.search.common.cache.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.erosnow.search.common.cache.Cache;
import com.erosnow.search.common.cache.impl.BlockingCacheWithTTL.EvictionListener;

@Cache(name = "aggregatorConfigCache")
public class AggregatorConfigCache<K> {

	private Map<String, BlockingCacheWithTTL<K>> lruCacheMap = new HashMap<String, BlockingCacheWithTTL<K>>();

	public void addAggregatorConfig(String listener, BlockingCacheWithTTL<K> cache) {
		lruCacheMap.put(listener, cache);
	}

	public BlockingCacheWithTTL<K> getAggregatorConfig(String listener) {
		return lruCacheMap.get(listener);
	}

	public Collection<BlockingCacheWithTTL<K>> getAllAggregatorConfig() {
		return lruCacheMap.values();
	}

	public List<EvictionListener<K, Long>> getAllEvictListeners() {
		List<EvictionListener<K, Long>> list = new ArrayList<EvictionListener<K, Long>>();
		for (String key : lruCacheMap.keySet()) {
			EvictionListener<K, Long> evictionListener = lruCacheMap.get(key).getEvictionListener();
			if (evictionListener != null) {
				list.add(evictionListener);
			}
		}
		return list;
	}
}

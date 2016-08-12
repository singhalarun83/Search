package com.erosnow.search.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheManager {
	private static final Logger LOG = LoggerFactory.getLogger(CacheManager.class);

	private CacheManager() {
	}

	private static CacheManager _instance = new CacheManager();
	private Map<String, Object> _caches = new ConcurrentHashMap<String, Object>();

	public static CacheManager getInstance() {
		return _instance;
	}

	public Object getCache(String cacheName) {
		return _caches.get(cacheName);
	}

	@SuppressWarnings("unchecked")
	public <T> T getCache(Class<T> cacheClass) {
		if (cacheClass.isAnnotationPresent(Cache.class)) {
			return (T) getCache(cacheClass.getAnnotation(Cache.class).name());
		} else {
			LOG.error("@Cache annotation should be present for cache class:" + cacheClass.getName());
			throw new IllegalArgumentException(
					"@Cache annotation should be present for cache class:" + cacheClass.getName());
		}
	}

	public synchronized void setCache(Object cache) {
		Class<? extends Object> cacheClass = cache.getClass();
		if (cacheClass.isAnnotationPresent(Cache.class)) {
			Cache annotation = cacheClass.getAnnotation(Cache.class);
			_caches.put(annotation.name(), cache);
		} else {
			LOG.error("@Cache annotation should be present for cache class:" + cache.getClass().getName());
			throw new IllegalArgumentException(
					"@Cache annotation should be present for cache class:" + cache.getClass().getName());
		}

	}
}

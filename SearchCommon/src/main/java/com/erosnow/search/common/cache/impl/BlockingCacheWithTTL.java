package com.erosnow.search.common.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.log.DebugLogger;
import com.erosnow.search.common.util.SearchPropertyEnum;

public class BlockingCacheWithTTL<K> {

	private static final Logger LOG = LoggerFactory.getLogger(BlockingCacheWithTTL.class);

	private Map<K, Long> cacheMap;

	private final int maxSize;

	private final int ttl;

	private EvictionListener<K, Long> evictionListener;

	private final CleanupThread<K, Long> cleanupThread;

	private final int cleanupInverval;

	private final String cacheIndentifier;

	private AtomicInteger sizeFull = new AtomicInteger(0);

	public BlockingCacheWithTTL(final int maxSize, final int ttlInMilliSec, EvictionListener<K, Long> evictionListener,
			String cacheIndentifier) {
		this.cacheMap = new ConcurrentHashMap<K, Long>();
		this.setEvictionListener(evictionListener);
		this.maxSize = maxSize;
		this.ttl = ttlInMilliSec;
		this.cleanupThread = new CleanupThread<K, Long>(this);
		int defaultClenupInterval = CacheManager.getInstance().getCache(SearchPropertyCache.class)
				.getIntegerProperty(SearchPropertyEnum.CACHE_CLEANUP_INTERVAL);
		this.cleanupInverval = (ttlInMilliSec < defaultClenupInterval) ? (ttlInMilliSec / 2) : defaultClenupInterval;
		this.cacheIndentifier = cacheIndentifier;
		scheduleCleanup();
	}

	public BlockingCacheWithTTL(final int maxSize, final int ttlInMilliSec, String cacheIndentifier) {
		this(maxSize, ttlInMilliSec, null, cacheIndentifier);
	}

	public boolean exists(K key) {
		if (cacheMap.get(key) != null) {
			return true;
		}
		return false;
	}

	public Long get(K key) {
		return cacheMap.get(key);
	}

	public boolean put(K key) {
		Long cacheEntryNode = cacheMap.get(key);
		if (cacheEntryNode != null) {
			DebugLogger.log(cacheIndentifier + " : Repeat for key : " + key);
			return true;
		}
		try {
			while (cacheMap.size() > maxSize) { // If size is full, do not add
												// in the cache
				LOG.error("Size is full : " + maxSize + ", cant put pog : " + key + " in cache. " + " Waitnig.. ");
				synchronized (sizeFull) {
					sizeFull.wait();
				}
				LOG.error("Waking up from size full condition : ");
			}
		} catch (InterruptedException e) {
			LOG.error("Exception caught while waiting : ", e);
		}
		long expiryTime = getExpiryTime(ttl);
		cacheMap.put(key, expiryTime);
		DebugLogger
				.log(cacheIndentifier + " : Created node with key : " + key + " expiry : " + new DateTime(expiryTime));
		return true;
	}

	public long getExpiryTime(long ttl) {
		DateTime now = new DateTime();
		return now.plus(ttl).getMillis();
	}

	private void scheduleCleanup() {
		ThreadPoolTaskScheduler cleanupScheduler = new ThreadPoolTaskScheduler();
		cleanupScheduler.initialize();
		cleanupScheduler.scheduleAtFixedRate(this.cleanupThread, this.cleanupInverval);
	}

	public interface EvictionListener<K1, V1> {
		public void evict(K1 key, V1 value);
	}

	private class CleanupThread<K1, V1> implements Runnable {
		private BlockingCacheWithTTL<K1> cache;

		public CleanupThread(BlockingCacheWithTTL<K1> lruCacheWithTTL) {
			this.cache = lruCacheWithTTL;
		}

		@Override
		public void run() {
			doCleanup();
		}

		private void doCleanup() {
			DebugLogger.log(cacheIndentifier + " : Starting scheduled cleanup process for EvictionCache of size : "
					+ cache.cacheMap.size());
			try {
				for (K1 key : cache.cacheMap.keySet()) {
					Long exp = cache.cacheMap.get(key);
					if (new DateTime().getMillis() >= exp) {
						removeCacheEntry(key);
					}
				}
				if (cache.cacheMap.size() < maxSize * 0.9) {
					synchronized (cache.sizeFull) {
						cache.sizeFull.notifyAll();
					}
				}
			} catch (Exception e) {
				LOG.error(cacheIndentifier + " : Error while performing clean up on LRU Cache : ", e);
			} finally {

			}
		}

		private void removeCacheEntry(K1 key) {
			Long expiry = cache.cacheMap.remove(key);
			LOG.info(cacheIndentifier + " : Removing entry key : " + key + " value : " + expiry);
			if (cache.getEvictionListener() != null) {
				cache.getEvictionListener().evict(key, expiry);
			} else {
				DebugLogger.log("No Eviction listener present. Evicted entry : " + key);
			}
		}
	}

	public EvictionListener<K, Long> getEvictionListener() {
		return evictionListener;
	}

	public void setEvictionListener(EvictionListener<K, Long> evictionListener) {
		this.evictionListener = evictionListener;
	}

}

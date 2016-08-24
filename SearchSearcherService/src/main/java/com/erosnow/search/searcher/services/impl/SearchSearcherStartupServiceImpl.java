package com.erosnow.search.searcher.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.entity.SearchProperty;
import com.erosnow.search.common.service.SearchConfigurationService;
import com.erosnow.search.common.util.CommonUtil;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.searcher.services.SearchSearcherStartupService;

@Component("searchSearcherStartupService")
public class SearchSearcherStartupServiceImpl implements SearchSearcherStartupService, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(SearchSearcherStartupServiceImpl.class);
	@Autowired
	private SearchConfigurationService searchConfigurationService;

	@Autowired
	private TaskScheduler reloadCacheScheduler;

	@Autowired
	private Environment environment;

	WebApplicationContext applicationContext;

	@PostConstruct
	public void loadAllAtStartup() {
		LOG.info("Load at startup:start");
		loadAllReloadable();
		initReloadCache();
		LOG.info("Load at startup:end");
	}

	public void loadAllReloadable() {
		LOG.info("Reloadable:start");
		loadSearchProperties();
		LOG.info("Reloadable:end");
	}

	private void initReloadCache() {
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		if (cache.getBooleanProperty(SearchPropertyEnum.RELOAD_CACHE_ENABLED)) {
			long interval = cache.getLongProperty(SearchPropertyEnum.RELOAD_CACHE_INTERVAL);
			reloadCacheScheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						loadAllReloadable();
					} catch (Exception ex) {
						LOG.error("Error in reloading thread", ex);
					}
				}
			}, cache.getReloadCacheReferenceTime(), interval);
		}
	}

	public void loadSearchProperties() {
		LOG.info("Load Search Property:start");
		List<SearchProperty> properties = searchConfigurationService.loadAllSearchProperties();
		SearchPropertyCache cache = new SearchPropertyCache(properties);
		cache.overrideFileProperties(CommonUtil.getAllKnownProperties(environment));
		// SearchPropertyCache oldCache =
		// CacheManager.getInstance().getCache(SearchPropertyCache.class);
		CacheManager.getInstance().setCache(cache);
		LOG.info("Load Search Property:end");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (WebApplicationContext) applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}

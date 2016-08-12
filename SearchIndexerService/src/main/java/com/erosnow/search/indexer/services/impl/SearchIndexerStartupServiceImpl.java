package com.erosnow.search.indexer.services.impl;

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
import com.erosnow.search.common.cache.impl.AggregatorConfigCache;
import com.erosnow.search.common.cache.impl.IndexerConfigurationCache;
import com.erosnow.search.common.cache.impl.ProducerConsumerConfigCache;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.entity.IndexerConfiguration;
import com.erosnow.search.common.entity.ProducerConsumerConfiguration;
import com.erosnow.search.common.entity.SearchProperty;
import com.erosnow.search.common.metrics.Metrics;
import com.erosnow.search.common.metrics.MetricsReporter;
import com.erosnow.search.common.service.SearchConfigurationService;
import com.erosnow.search.common.util.CommonUtil;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.SearchIndexerServiceFactory;
import com.erosnow.search.indexer.services.SearchIndexerStartupService;
import com.erosnow.search.indexer.services.consumer.ConsumerSpawnService;

@Component("searchIndexerStartupService")
public class SearchIndexerStartupServiceImpl implements SearchIndexerStartupService, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(SearchIndexerStartupServiceImpl.class);
	@Autowired
	private SearchConfigurationService searchConfigurationService;

	@Autowired
	private TaskScheduler reloadCacheScheduler;

	@Autowired
	private Environment environment;

	WebApplicationContext applicationContext;

	@Autowired
	private SearchIndexerServiceFactory searchIndexerServiceFactory;

	@PostConstruct
	public void loadAllAtStartup() {
		LOG.info("Load at startup:start");
		loadAllReloadable();
		loadProducerConsumerConfigurations();
		loadAggregatorConfigCache();
		initReloadCache();
		initFlushingScheduler();
		initGraphiteReporting();
		LOG.info("Load at startup:end");
	}

	public void initGraphiteReporting() {
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		if (cache.getBooleanProperty(SearchPropertyEnum.GRAPHITE_REPORT_ENABLE)) {
			LOG.info("Starting graphite...");
			applicationContext.getServletContext().setAttribute(Metrics.REGISTRY_ATTRIBUTE, Metrics.getRegistry());
			MetricsReporter reporter = new MetricsReporter();
			String component = cache.getProperty(SearchPropertyEnum.GRAPHITE_COMPONENT_INDEXER);
			String env = cache.getProperty(SearchPropertyEnum.APPLICATION_ENVIRONMENT);
			String host = cache.getProperty(SearchPropertyEnum.GRAPHITE_HOST);
			int port = cache.getIntegerProperty(SearchPropertyEnum.GRAPHITE_PORT);
			reporter.reportToGraphite(env + component, host, port);
			LOG.info("Started graphite successfully...");
		}

	}

	public void loadAllReloadable() {
		LOG.info("Reloadable:start");
		loadSearchProperties();
		loadIndexerConfigurations();
		LOG.info("Reloadable:end");
	}
	
	@SuppressWarnings("rawtypes")
	public void loadAggregatorConfigCache() {
        LOG.info("Loading Aggregator Config Cache..");
        AggregatorConfigCache configCache = new AggregatorConfigCache();
        CacheManager.getInstance().setCache(configCache);
        LOG.info("Aggregator Config Cache loaded SUCCESSFULLY");
    }

	public void loadIndexerConfigurations() {
		LOG.info("Loading Indexer Configurations...");
		IndexerConfigurationCache configCache = new IndexerConfigurationCache();
		try {
			List<IndexerConfiguration> indexers = searchConfigurationService.loadAllIndexerConfigurations();
			LOG.info(indexers.toString());
			for (IndexerConfiguration config : indexers) {
				if (config.isEnabled()) {
					configCache.addIndexerConfiguration(config);
				}
			}
			List<ConsumerTypeConfiguration> consumers = searchConfigurationService.loadAllConsumerTypeConfigurations();
			LOG.info(consumers.toString());
			for (ConsumerTypeConfiguration config : consumers) {
				if (config.isEnabled()) {
					configCache.addConsumerTypeConfiguration(config);
				}
			}
		} catch (Exception e) {
			LOG.error("Error while loading indexer configurations", e);
		}
		CacheManager.getInstance().setCache(configCache);
		LOG.info("Loaded Indexer Configurations... SUCCESSFULLY");
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

	private void initFlushingScheduler() {
		reloadCacheScheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				List<ConsumerSpawnService> services = searchIndexerServiceFactory.getAllFlushableServices();
				for (ConsumerSpawnService service : services) {
					service.flushAll();
				}
			}
		}, CacheManager.getInstance().getCache(SearchPropertyCache.class)
				.getIntegerProperty(SearchPropertyEnum.INDEXER_FLUSH_INTERVAL));
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

	public void loadProducerConsumerConfigurations() {
		LOG.info("Loading Producer Consumer Configurations...");
		ProducerConsumerConfigCache configCache = new ProducerConsumerConfigCache();
		SearchPropertyCache searchCache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		List<ProducerConsumerConfiguration> producerConsumerConfigs = searchConfigurationService
				.loadAllProducerConsumerConfigurations();
		try {
			configCache.setConfig(producerConsumerConfigs);
			// configCache.loadConfig(searchCache.getProperty("producerConsumer.config.filepath"));
		} catch (Exception e) {
			LOG.error("Error while loading producerConsumer config from : "
					+ searchCache.getProperty("producerConsumer.config.filepath"), e);
		}
		// ProducerConsumerConfigCache oldCache =
		// CacheManager.getInstance().getCache(ProducerConsumerConfigCache.class);
		CacheManager.getInstance().setCache(configCache);
		LOG.info("Loaded Producer Consumer Configurations... SUCCESSFULLY");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (WebApplicationContext) applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}

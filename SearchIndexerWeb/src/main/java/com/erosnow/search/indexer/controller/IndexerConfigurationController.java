package com.erosnow.search.indexer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.SearchIndexerStartupService;

@RestController
@RequestMapping("/service/searchIndexer/")
public class IndexerConfigurationController {

	@Autowired
	SearchIndexerStartupService searchIndexerStartupService;

	private static final Logger LOG = LoggerFactory.getLogger(IndexerConfigurationController.class);

	@RequestMapping("")
	public String healthCheck() {
		return "Congratulations. It works!";
	}

	@RequestMapping(value = "reloadcache")
	public String reloadCache(@RequestParam(value = "pwd") String password) {
		if (CacheManager.getInstance().getCache(SearchPropertyCache.class)
				.getProperty(SearchPropertyEnum.RELOAD_CACHE_PASSWORD).equals(password)) {
			try {
				searchIndexerStartupService.loadAllReloadable();
				return "Indexer Cache Reloaded SUCCESSFULLY ...";
			} catch (Exception e) {
				LOG.error("Exception while reloading cache: ", e);
				return "unable to reload cache ... check logs for details";
			}
		} else {
			return "Authentication FAILURE !!!";
		}
	}
}

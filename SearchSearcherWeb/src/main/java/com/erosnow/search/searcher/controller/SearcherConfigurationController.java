package com.erosnow.search.searcher.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.searcher.services.SearchSearcherStartupService;

@RestController
@RequestMapping("/service/searchSearcher/")
public class SearcherConfigurationController {

	@Autowired
	SearchSearcherStartupService searchSearcherStartupService;

	private static final Logger LOG = LoggerFactory.getLogger(SearcherConfigurationController.class);

	@RequestMapping("")
	public String healthCheck() {
		return "Congratulations. Searcher works!";
	}

	@RequestMapping(value = "reloadcache")
	public String reloadCache(@RequestParam(value = "pwd") String password) {
		if (CacheManager.getInstance().getCache(SearchPropertyCache.class)
				.getProperty(SearchPropertyEnum.RELOAD_CACHE_PASSWORD).equals(password)) {
			try {
				searchSearcherStartupService.loadAllReloadable();
				return "Searcher Cache Reloaded SUCCESSFULLY ...";
			} catch (Exception e) {
				LOG.error("Exception while reloading cache: ", e);
				return "unable to reload cache ... check logs for details";
			}
		} else {
			return "Authentication FAILURE !!!";
		}
	}

}

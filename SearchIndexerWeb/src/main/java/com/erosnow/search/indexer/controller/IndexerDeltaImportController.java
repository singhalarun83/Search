package com.erosnow.search.indexer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.dataImport.dao.AbstractDao;
import com.erosnow.search.indexer.services.producer.RequeueService;

@RestController("indexerDeltaImportController")
@RequestMapping("/service/searchIndexer/")
public class IndexerDeltaImportController extends IndexerAbstractImportController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexerDeltaImportController.class);

	@RequestMapping("deltaImportAllContents")
	@Async
	public void deltaImportAllContents(
			@RequestParam(required = false, value = "print", defaultValue = "false") boolean print,
			@RequestParam(required = false, value = "requeue", defaultValue = "true") boolean requeue,
			@RequestParam(required = false, value = "interval", defaultValue = "1") Integer interval) throws Exception {
		if (interval == null)
			interval = CacheManager.getInstance().getCache(SearchPropertyCache.class)
					.getIntegerProperty(SearchPropertyEnum.INDEXER_DELTA_QUERY_INTERVAL);
		print(contentService.getDeltaContentIds(AbstractDao.CONTENT_ASSET_DELTA_QUERY, interval), requeue, print,
				RequeueService.CONTENT_LISTENERS);
	}

}

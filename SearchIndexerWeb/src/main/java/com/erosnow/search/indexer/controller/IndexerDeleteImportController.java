package com.erosnow.search.indexer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.Listener;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.dataImport.dao.AbstractDao;

@RestController("indexerDeleteImportController")
@RequestMapping("/service/searchIndexer/")
public class IndexerDeleteImportController extends IndexerAbstractImportController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexerDeleteImportController.class);

	@RequestMapping("deleteImportAllContents")
	@Async
	public void deleteImportAllContents(
			@RequestParam(required = false, value = "print", defaultValue = "false") boolean print,
			@RequestParam(required = false, value = "requeue", defaultValue = "true") boolean requeue,
			@RequestParam(required = false, value = "interval", defaultValue = "1") Integer interval) throws Exception {
		if (interval == null)
			interval = CacheManager.getInstance().getCache(SearchPropertyCache.class)
					.getIntegerProperty(SearchPropertyEnum.INDEXER_DELETE_QUERY_INTERVAL);
		print(contentService.getDeltaContentIds(AbstractDao.CONTENT_DELETE_QUERY, interval), requeue, print,
				Listener.DELETE_CONTENT_KAFKA_LISTENER);
	}
}

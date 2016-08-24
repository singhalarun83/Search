package com.erosnow.search.indexer.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.erosnow.search.indexer.services.dataImport.service.ContentService;
import com.erosnow.search.indexer.services.dataImport.service.PlaylistService;
import com.erosnow.search.indexer.services.dto.KafkaPushDTO;
import com.erosnow.search.indexer.services.producer.RequeueService;

public class IndexerAbstractImportController {

	@Autowired
	protected ContentService contentService;

	@Autowired
	protected PlaylistService playlistService;

	@Autowired
	protected RequeueService requeueService;

	private static final Logger LOG = LoggerFactory.getLogger(IndexerAbstractImportController.class);

	protected void print(List<Map<String, Object>> contents, boolean requeue, boolean print, String... listeners)
			throws Exception {
		for (Map<String, Object> content : contents) {
			if (print)
				LOG.info(content.toString());
			if (requeue && listeners != null) {
				String contentId = content.get("unique_id").toString();
				for (String listener : listeners) {
					KafkaPushDTO dto = new KafkaPushDTO(contentId, Long.parseLong(contentId));
					requeueService.requeue(listener, dto);
				}
			}
		}
	}
}

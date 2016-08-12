package com.erosnow.search.indexer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("indexerImportController")
@RequestMapping("/service/searchIndexer/")
public class IndexerImportController extends IndexerAbstractImportController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexerImportController.class);

	@RequestMapping("importAllContents")
	@Async
	public void importAllContents(@RequestParam(required = false, value = "print", defaultValue = "false") boolean print,
			@RequestParam(required = false, value = "requeue", defaultValue = "true") boolean requeue)
					throws Exception {
		print(contentService.getAllContentIds(), requeue, print, CONTENT_LISTENERS);
	}

	@RequestMapping("importAllPlaylists")
	@Async
	public void importAllPlaylists(@RequestParam(required = false, value = "print", defaultValue = "false") boolean print,
			@RequestParam(required = false, value = "requeue", defaultValue = "true") boolean requeue)
					throws Exception {
		print(playlistService.getAllPlaylistIds(), requeue, print, PLAYLIST_LISTENERS);
	}

}

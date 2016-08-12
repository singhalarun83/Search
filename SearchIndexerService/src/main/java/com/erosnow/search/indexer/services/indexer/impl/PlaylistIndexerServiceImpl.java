package com.erosnow.search.indexer.services.indexer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erosnow.search.indexer.services.dataImport.service.PlaylistService;
import com.erosnow.search.indexer.services.dto.SolrDocumentBuilder;
import com.erosnow.search.indexer.services.indexer.PlaylistIndexerService;
import com.erosnow.search.indexer.services.indexer.SolrIndexerService;
import com.erosnow.search.indexer.services.util.SolrDocumentUtil;

@Service("playlistIndexerService")
public class PlaylistIndexerServiceImpl implements PlaylistIndexerService {

	@Autowired
	private PlaylistService playlistService;

	@Autowired
	private SolrIndexerService solrIndexerService;

	public void indexPlaylistDetails(List<String> playlistIds) {
		List<Map<String, Object>> playlistDetails = playlistService.getPlaylistDetailsByPlaylistIds(playlistIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> playlistDetail : playlistDetails) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildPlaylistDetailDocument(playlistDetail);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexPlaylistImages(List<String> playlistIds) {
		List<Map<String, Object>> playlistImages = playlistService.getPlaylistImagesByPlaylistIds(playlistIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> playlistImage : playlistImages) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildPlaylistImageDocument(playlistImage);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexPlaylistAllows(List<String> playlistIds) {
		List<Map<String, Object>> playlistAllows = playlistService.getPlaylistAllowsByPlaylistIds(playlistIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> playlistAllow : playlistAllows) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildPlaylistAllowDocument(playlistAllow);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

}

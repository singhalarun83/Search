package com.erosnow.search.indexer.services.dataImport.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erosnow.search.indexer.services.dataImport.dao.PlaylistDao;;

@Service("playlistService")
// @Transactional("contentTransactionManager")
public class PlaylistServiceImpl implements PlaylistService {

	@Autowired
	private PlaylistDao dao;

	public List<Map<String, Object>> getAllPlaylistIds() {
		return dao.getAllPlaylistIds();
	}

	public List<Map<String, Object>> getLimitedPlaylistIds(int limit) {
		if (limit == 0)
			limit = DEFAULT_PLAYLIST_LIMIT;
		return dao.getLimitedPlaylistIds(limit);
	}

	public List<Map<String, Object>> getPlaylistDetailsByPlaylistIds(List<String> playlistIds) {
		return dao.getPlaylistDetailsByPlaylistIds(playlistIds);
	}

	public List<Map<String, Object>> getPlaylistImagesByPlaylistIds(List<String> playlistIds) {
		return dao.getPlaylistImagesByPlaylistIds(playlistIds);
	}

	public List<Map<String, Object>> getPlaylistAllowsByPlaylistIds(List<String> playlistIds) {
		return dao.getPlaylistAllowsByPlaylistIds(playlistIds);
	}
}

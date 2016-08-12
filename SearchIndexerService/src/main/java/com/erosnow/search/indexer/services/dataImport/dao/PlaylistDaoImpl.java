package com.erosnow.search.indexer.services.dataImport.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("playlistDao")
public class PlaylistDaoImpl extends AbstractDao implements PlaylistDao {

	public List<Map<String, Object>> getAllPlaylistIds() {
		return getJdbcTemplate().queryForList(PLAYLIST_ID_QUERY);
	}

	public List<Map<String, Object>> getLimitedPlaylistIds(int limit) {
		return getJdbcTemplate().queryForList(PLAYLIST_ID_QUERY + "  limit  " + limit);
	}

	public List<Map<String, Object>> getPlaylistDetailsByPlaylistIds(List<String> playlistIds) {
		return getNamedParameterJdbcTemplate().queryForList(PLAYLIST_DETAIL_QUERY,
				Collections.singletonMap("ids", playlistIds));
	}

	public List<Map<String, Object>> getPlaylistImagesByPlaylistIds(List<String> playlistIds) {
		return getNamedParameterJdbcTemplate().queryForList(PLAYLIST_IMAGES_QUERY,
				Collections.singletonMap("ids", playlistIds));
	}

	public List<Map<String, Object>> getPlaylistAllowsByPlaylistIds(List<String> playlistIds) {
		return getNamedParameterJdbcTemplate().queryForList(PLAYLIST_ALLOW_QUERY,
				Collections.singletonMap("ids", playlistIds));
	}
}

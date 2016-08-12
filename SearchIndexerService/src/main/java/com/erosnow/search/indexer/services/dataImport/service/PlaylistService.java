package com.erosnow.search.indexer.services.dataImport.service;

import java.util.List;
import java.util.Map;

public interface PlaylistService {

	final int DEFAULT_PLAYLIST_LIMIT = 50;

	public List<Map<String, Object>> getAllPlaylistIds();

	public List<Map<String, Object>> getLimitedPlaylistIds(int limit);

	public List<Map<String, Object>> getPlaylistDetailsByPlaylistIds(List<String> playlistIds);

	public List<Map<String, Object>> getPlaylistImagesByPlaylistIds(List<String> playlistIds);

	public List<Map<String, Object>> getPlaylistAllowsByPlaylistIds(List<String> playlistIds);

}

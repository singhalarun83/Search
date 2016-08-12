package com.erosnow.search.indexer.services.indexer;

import java.util.List;

public interface PlaylistIndexerService {

	public void indexPlaylistDetails(List<String> playlistIds);

	public void indexPlaylistImages(List<String> playlistIds);

	public void indexPlaylistAllows(List<String> playlistIds);
}

package com.erosnow.search.indexer.services.indexer;

import java.util.List;

public interface ContentIndexerService {

	public void indexRoles(List<String> contentIds);

	public void indexAllowedOrBlocked(List<String> contentIds);

	public void indexContentDetails(List<String> contentIds);

	public void indexGenres(List<String> contentIds);

	public void indexKeywords(List<String> contentIds);

	public void indexParentContents(List<String> contentIds);

	public void indexContentAssetProducts(List<String> contentIds);

	public void indexContentProducts(List<String> contentIds);

	public void indexSubtitles(List<String> contentIds);

	public void indexRecommendations(List<String> contentIds);

	public void indexContentAssetImagePaths(List<String> contentIds);

	public void indexContentImagePaths(List<String> contentIds);

	public void indexContentAll(List<String> contentIds);

	public void indexBasicCounts(List<String> contentIds);

	public void indexPreminumCounts(List<String> contentIds);

	public void indexPreminumCounts_3_4(List<String> contentIds);

	public void indexPreminumCounts_34(List<String> contentIds);

	public void indexPreminumCounts_1(List<String> contentIds);

	public void indexTotalCounts(List<String> contentIds);

	public void indexTotalCounts_3_4(List<String> contentIds);

	public void indexTotalCounts_34(List<String> contentIds);

	public void indexTotalCounts_1(List<String> contentIds);

	public void indexProductMovies(List<String> contentIds);

	public void deleteById(List<String> uniqueIds);
}

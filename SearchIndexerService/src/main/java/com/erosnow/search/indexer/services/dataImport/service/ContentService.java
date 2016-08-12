package com.erosnow.search.indexer.services.dataImport.service;

import java.util.List;
import java.util.Map;

public interface ContentService {

	final int DEFAULT_CONTENT_LIMIT = 50;

	public List<Map<String, Object>> getAllContentIds();

	public List<Map<String, Object>> getLimitedContentIds(int limit);

	public List<Map<String, Object>> getContentDetailByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getParentContentByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getKeywordsByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getRecommendationsByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getRolesByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getGenresByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getContentAssetProductsByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getContentProductsByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getSubtitlesByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getAllowBlockByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getContentAssetImagePathsByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getContentImagePathsByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getBasicCountByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getPremiumCount_3_4_ByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getPremiumCount_34_ByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getPremiumCount_1_ByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getTotalCount_3_4_ByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getTotalCount_34_ByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getTotalCount_1_ByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getContentAllByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getProductMoviesByContentIds(List<String> contentIds);

	public List<Map<String, Object>> getDeltaContentIds(String query, int interval);
}

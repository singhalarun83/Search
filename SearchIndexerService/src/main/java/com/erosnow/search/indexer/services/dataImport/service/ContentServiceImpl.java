package com.erosnow.search.indexer.services.dataImport.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erosnow.search.indexer.services.dataImport.dao.ContentDao;

@Service("contentService")
// @Transactional("contentTransactionManager")
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentDao dao;

	public List<Map<String, Object>> getAllContentIds() {
		return dao.getAllContentIds();
	}

	public List<Map<String, Object>> getLimitedContentIds(int limit) {
		if (limit == 0)
			limit = DEFAULT_CONTENT_LIMIT;
		return dao.getLimitedContentIds(limit);
	}

	public List<Map<String, Object>> getContentDetailByContentIds(List<String> contentIds) {
		return dao.getContentDetailByContentIds(contentIds);
	}

	public List<Map<String, Object>> getParentContentByContentIds(List<String> contentIds) {
		return dao.getParentContentByContentIds(contentIds);
	}

	public List<Map<String, Object>> getKeywordsByContentIds(List<String> contentIds) {
		return dao.getKeywordsByContentIds(contentIds);
	}

	public List<Map<String, Object>> getRecommendationsByContentIds(List<String> contentIds) {
		return dao.getRecommendationsByContentIds(contentIds);
	}

	public List<Map<String, Object>> getRolesByContentIds(List<String> contentIds) {
		return dao.getRolesByContentIds(contentIds);
	}

	public List<Map<String, Object>> getGenresByContentIds(List<String> contentIds) {
		return dao.getGenresByContentIds(contentIds);
	}

	public List<Map<String, Object>> getContentAssetProductsByContentIds(List<String> contentIds) {
		return dao.getContentAssetProductsByContentIds(contentIds);
	}

	public List<Map<String, Object>> getContentProductsByContentIds(List<String> contentIds) {
		return dao.getContentProductsByContentIds(contentIds);
	}

	public List<Map<String, Object>> getSubtitlesByContentIds(List<String> contentIds) {
		return dao.getSubtitlesByContentIds(contentIds);
	}

	public List<Map<String, Object>> getAllowBlockByContentIds(List<String> contentIds) {
		return dao.getAllowBlockByContentIds(contentIds);
	}

	public List<Map<String, Object>> getContentAssetImagePathsByContentIds(List<String> contentIds) {
		return dao.getContentAssetImagePathsByContentIds(contentIds);
	}

	public List<Map<String, Object>> getContentImagePathsByContentIds(List<String> contentIds) {
		return dao.getContentImagePathsByContentIds(contentIds);
	}

	public List<Map<String, Object>> getBasicCountByContentIds(List<String> contentIds) {
		return dao.getBasicCountByContentIds(contentIds);
	}

	public List<Map<String, Object>> getPremiumCount_3_4_ByContentIds(List<String> contentIds) {
		return dao.getPremiumCount_3_4_ByContentIds(contentIds);
	}

	public List<Map<String, Object>> getPremiumCount_34_ByContentIds(List<String> contentIds) {
		return dao.getPremiumCount_34_ByContentIds(contentIds);
	}

	public List<Map<String, Object>> getPremiumCount_1_ByContentIds(List<String> contentIds) {
		return dao.getPremiumCount_1_ByContentIds(contentIds);
	}

	public List<Map<String, Object>> getTotalCount_3_4_ByContentIds(List<String> contentIds) {
		return dao.getTotalCount_3_4_ByContentIds(contentIds);
	}

	public List<Map<String, Object>> getTotalCount_34_ByContentIds(List<String> contentIds) {
		return dao.getTotalCount_34_ByContentIds(contentIds);
	}

	public List<Map<String, Object>> getTotalCount_1_ByContentIds(List<String> contentIds) {
		return dao.getTotalCount_1_ByContentIds(contentIds);
	}

	public List<Map<String, Object>> getContentAllByContentIds(List<String> contentIds) {
		return dao.getContentAllByContentIds(contentIds);
	}

	public List<Map<String, Object>> getProductMoviesByContentIds(List<String> contentIds) {
		return dao.getProductMoviesByContentIds(contentIds);
	}

	public List<Map<String, Object>> getDeltaContentIds(String query, int interval) {
		return dao.getDeltaContentIds(query, interval);
	}
}

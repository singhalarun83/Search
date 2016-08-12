package com.erosnow.search.indexer.services.dataImport.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository("contentDao")
public class ContentDaoImpl extends AbstractDao implements ContentDao {

	public List<Map<String, Object>> getAllContentIds() {
		return getJdbcTemplate().queryForList(CONTENT_ASSET_QUERY);
	}

	public List<Map<String, Object>> getLimitedContentIds(int limit) {
		return getJdbcTemplate().queryForList(CONTENT_ASSET_QUERY + "  limit  " + limit);
	}

	public List<Map<String, Object>> getContentDetailByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_DETAIL_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getParentContentByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_PARENT_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getKeywordsByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_KEYWORD_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getRecommendationsByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_RECOMMENDATION_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getRolesByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_ROLES_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getGenresByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_GENRE_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getContentAssetProductsByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_PRODUCT_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getContentProductsByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_PRODUCT_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getSubtitlesByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_SUBTITLE_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getAllowBlockByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_ALLOW_BLOCK_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getContentImagePathsByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_IMAGES_PATH_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getContentAssetImagePathsByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_IMAGES_PATH_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getBasicCountByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_BASIC_COUNT_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getPremiumCount_3_4_ByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_PREMIUM_COUNT_3_4_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getPremiumCount_34_ByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_PREMIUM_COUNT_34_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getPremiumCount_1_ByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_PREMIUM_COUNT_1_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getTotalCount_3_4_ByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_TOTAL_COUNT_3_4_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getTotalCount_34_ByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_TOTAL_COUNT_34_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getTotalCount_1_ByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_TOTAL_COUNT_1_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getContentAllByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_CONTENT_ALL_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getProductMoviesByContentIds(List<String> contentIds) {
		return getNamedParameterJdbcTemplate().queryForList(CONTENT_ASSET_PRODUCT_MOVIES_QUERY,
				Collections.singletonMap("ids", contentIds));
	}

	public List<Map<String, Object>> getDeltaContentIds(String query, int interval) {
		SqlParameterSource namedParameters = new MapSqlParameterSource("interval", Integer.valueOf(interval));
		return getNamedParameterJdbcTemplate().queryForList(query, namedParameters);
	}
}

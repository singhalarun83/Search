package com.erosnow.search.indexer.services.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.erosnow.search.common.util.SolrFields;
import com.erosnow.search.indexer.services.dto.SolrDocumentBuilder;

public class SolrDocumentUtil {

	private static SolrDocumentBuilder handleAllAsMultivalued(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap, null, false, null);
	}

	private static SolrDocumentBuilder handleAllAsMultivalued(Map<String, Object> multiFieldMap,
			Map<String, String> multiExclusion) {
		return handleAllAsMultivalued(multiFieldMap, multiExclusion, false, null);
	}

	private static SolrDocumentBuilder handleAllAsMultivalued(Map<String, Object> multiFieldMap, boolean ignoreMulti) {
		return handleAllAsMultivalued(multiFieldMap, null, ignoreMulti, null);
	}

	private static SolrDocumentBuilder handleAllAsMultivalued(Map<String, Object> multiFieldMap,
			Map<String, String> multiExclusion, boolean ignoreMulti, Map<String, String> multiInclusion) {
		SolrDocumentBuilder sdb = SolrDocumentBuilder.newInstance().setId(SolrFields.ID,
				multiFieldMap.get(SolrFields.ID));
		// sdb.setId(SolrFields.SOLR_ID, multiFieldMap.get(SolrFields.ID));
		for (Map.Entry<String, Object> field : multiFieldMap.entrySet()) {
			/*
			 * if (field.getValue() == null ||
			 * field.getValue().toString().length() == 0) continue;
			 */
			if ((multiExclusion != null && multiExclusion.containsKey(field.getKey())) || ignoreMulti
					|| (multiInclusion != null && !multiInclusion.containsKey(field.getKey()))) {
				sdb.set(field.getKey(), field.getValue());
			} else {
				if (field.getValue() != null)
					sdb.set(field.getKey(), Arrays.asList(field.getValue().toString().split(",")));
				else
					sdb.set(field.getKey(), field.getValue());
			}
		}
		return sdb;
	}

	public static SolrDocumentBuilder buildContentDetailsDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap, true);
	}

	public static SolrDocumentBuilder buildRolesDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildAllowBlocksDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildContentAssetImagePathsDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildContentImagePathsDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildGenresDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildKeywordsDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildRecommendations(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildParentContentsDocument(Map<String, Object> multiFieldMap) {
		Map<String, String> multiExclusion = new HashMap<String, String>();
		multiExclusion.put(SolrFields.PARENT_CONTENT_ID, null);
		return handleAllAsMultivalued(multiFieldMap, multiExclusion);
	}

	public static SolrDocumentBuilder buildContentAllDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildBasicCountsDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildPremiumCountsDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildTotalCountsDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildProductMoviesDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildContentAssetProductsDocument(Map<String, Object> multiFieldMap) {
		Map<String, String> multiExclusion = new HashMap<String, String>();
		multiExclusion.put(SolrFields.PRODUCT_COUNT, null);
		return handleAllAsMultivalued(multiFieldMap, multiExclusion);
	}

	public static SolrDocumentBuilder buildContentProductsDocument(Map<String, Object> multiFieldMap) {
		Map<String, String> multiExclusion = new HashMap<String, String>();
		multiExclusion.put(SolrFields.CONTACT_PRODUCT_COUNT, null);
		return handleAllAsMultivalued(multiFieldMap, multiExclusion);
	}

	public static SolrDocumentBuilder buildSubtitlesDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildPlaylistDetailDocument(Map<String, Object> multiFieldMap) {
		Map<String, String> multiInclusion = new HashMap<String, String>();
		multiInclusion.put(SolrFields.PLAYLIST_IMAGES, null);
		multiInclusion.put(SolrFields.PLAYLIST_IMAGE_TYPE, null);
		multiInclusion.put(SolrFields.PRODUCT, null);
		multiInclusion.put(SolrFields.SINGER, null);
		multiInclusion.put(SolrFields.LYRICIST, null);
		multiInclusion.put(SolrFields.MUSIC_DIRECTOR, null);
		multiInclusion.put(SolrFields.ACTOR, null);
		multiInclusion.put(SolrFields.DIRECTOR, null);
		multiInclusion.put(SolrFields.PLAYBACK, null);
		multiInclusion.put(SolrFields.PRODUCER, null);
		return handleAllAsMultivalued(multiFieldMap, null, false, multiInclusion);
	}

	public static SolrDocumentBuilder buildPlaylistImageDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildPlaylistAllowDocument(Map<String, Object> multiFieldMap) {
		return handleAllAsMultivalued(multiFieldMap);
	}

	public static SolrDocumentBuilder buildToBeDeletedDocument(String uniqueId) {
		SolrDocumentBuilder sdb = SolrDocumentBuilder.newInstance().setId(SolrFields.ID, uniqueId);
		sdb.set("deleteThis", true);
		return sdb;
	}
}

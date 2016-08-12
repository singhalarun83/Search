package com.erosnow.search.indexer.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erosnow.search.common.util.Listener;
import com.erosnow.search.indexer.services.dataImport.service.ContentService;
import com.erosnow.search.indexer.services.dataImport.service.PlaylistService;
import com.erosnow.search.indexer.services.dto.KafkaPushDTO;
import com.erosnow.search.indexer.services.producer.RequeueService;

@RestController
@RequestMapping("/service/searchIndexer/")
public class IndexerTestController {

	@Autowired
	ContentService contentService;

	@Autowired
	PlaylistService playlistService;

	@Autowired
	RequeueService requeueService;

	private static final Logger LOG = LoggerFactory.getLogger(IndexerTestController.class);

	private List<String> getTestContentIds(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			return Arrays.asList(ids.split(","));
		}
		return Arrays.asList(new String[] { "1000004", "6140278", "6173470", "6124600", "6125592", "6091232", "6000141",
				"6125593", "1000013" });
	}

	private List<String> getTestPlaylistIds(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			return Arrays.asList(ids.split(","));
		}
		return Arrays.asList(new String[] { "1006072" });
	}

	private void print(List<Map<String, Object>> contents, boolean requeue, String... listeners) throws Exception {
		for (Map<String, Object> content : contents) {
			LOG.info(content.toString());
			if (requeue && listeners != null) {
				Long uniqueId = getUniqueId(content.get("unique_id").toString());
				KafkaPushDTO dto = new KafkaPushDTO(uniqueId.toString(), uniqueId);
				for (String listener : listeners)
					requeueService.requeue(listener, dto);
			}
		}
	}

	private Long getUniqueId(String uniqueId) {
		if (uniqueId == null)
			return null;
		if (uniqueId.indexOf("-") != -1)
			return 1006072l;
		return new Long(uniqueId);
	}

	private void print(List<Map<String, Object>> contents) {
		try {
			print(contents, false, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping("testAllContent")
	public void testAllContent(@RequestParam(required = false, value = "limit", defaultValue = "10") int limit)
			throws Exception {
		String arr[] = new String[] { Listener.CONTENT_KAFKA_LISTENER, Listener.ROLE_KAFKA_LISTENER,
				Listener.KEYWORD_KAFKA_LISTENER, Listener.RECOMMENDATION_KAFKA_LISTENER,
				Listener.CONTENT_IMAGEPATH_KAFKA_LISTENER, Listener.CONTENT_ASSET_IMAGEPATH_KAFKA_LISTENER,
				Listener.CONTENT_ALL_KAFKA_LISTENER, Listener.CONTENT_ASSET_PRODUCT_KAFKA_LISTENER,
				Listener.CONTENT_PRODUCT_KAFKA_LISTENER, Listener.PRODUCT_MOVIES_KAFKA_LISTENER,
				Listener.PREMIUM_COUNT_KAFKA_LISTENER, Listener.BASIC_COUNT_KAFKA_LISTENER,
				Listener.TOTAL_COUNT_KAFKA_LISTENER, Listener.ALLOW_BLOCK_KAFKA_LISTENER, Listener.GENRE_KAFKA_LISTENER,
				Listener.PARENT_CONTENT_KAFKA_LISTENER, Listener.SUBTITLE_KAFKA_LISTENER };
		print(contentService.getLimitedContentIds(limit), true, arr);
	}

	@RequestMapping("testContentByIds")
	public void testContentByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getContentDetailByContentIds(getTestContentIds(ids)), index,
				Listener.CONTENT_KAFKA_LISTENER);
	}

	@RequestMapping("testParentContentByIds")
	public void testParentContentByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getParentContentByContentIds(getTestContentIds(ids)), index,
				Listener.PARENT_CONTENT_KAFKA_LISTENER);
	}

	@RequestMapping("testKeywordsByIds")
	public void testKeywordsByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getKeywordsByContentIds(getTestContentIds(ids)), index, Listener.KEYWORD_KAFKA_LISTENER);
	}

	@RequestMapping("testRecommendationsByIds")
	public void testRecommendationsByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getRecommendationsByContentIds(getTestContentIds(ids)), index,
				Listener.RECOMMENDATION_KAFKA_LISTENER);
	}

	@RequestMapping("testRolesByIds")
	public void testRolesByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getRolesByContentIds(getTestContentIds(ids)), index, Listener.ROLE_KAFKA_LISTENER);
	}

	@RequestMapping("testContentAssetProductsByIds")
	public void testContentAssetProductsByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getContentAssetProductsByContentIds(getTestContentIds(ids)), index,
				Listener.CONTENT_ASSET_PRODUCT_KAFKA_LISTENER);
	}

	@RequestMapping("testContentProductsByIds")
	public void testContentProductsByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getContentProductsByContentIds(getTestContentIds(ids)), index,
				Listener.CONTENT_PRODUCT_KAFKA_LISTENER);
	}

	@RequestMapping("testSubtitlesByIds")
	public void testSubtitlesByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getSubtitlesByContentIds(getTestContentIds(ids)), index, Listener.SUBTITLE_KAFKA_LISTENER);
	}

	@RequestMapping("testGenresByIds")
	public void testGenresByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getGenresByContentIds(getTestContentIds(ids)), index, Listener.GENRE_KAFKA_LISTENER);
	}

	@RequestMapping("testAllowBlockByIds")
	public void testAllowBlockByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getAllowBlockByContentIds(getTestContentIds(ids)), index,
				Listener.ALLOW_BLOCK_KAFKA_LISTENER);
	}

	@RequestMapping("testContentAssetImagePathsByIds")
	public void testContentAssetImagePathsByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getContentAssetImagePathsByContentIds(getTestContentIds(ids)), index,
				Listener.CONTENT_ASSET_IMAGEPATH_KAFKA_LISTENER);
	}

	@RequestMapping("testContentImagePathsByIds")
	public void testContentImagePathsByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getContentImagePathsByContentIds(getTestContentIds(ids)), index,
				Listener.CONTENT_IMAGEPATH_KAFKA_LISTENER);
	}

	@RequestMapping("testBasicCountByIds")
	public void testBasicCountByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getBasicCountByContentIds(getTestContentIds(ids)), index,
				Listener.BASIC_COUNT_KAFKA_LISTENER);
	}

	@RequestMapping("testPremiumCountByIds")
	public void testPremiumCountByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getPremiumCount_3_4_ByContentIds(getTestContentIds(ids)), false,
				Listener.PREMIUM_COUNT_KAFKA_LISTENER);
		print(contentService.getPremiumCount_34_ByContentIds(getTestContentIds(ids)), false,
				Listener.PREMIUM_COUNT_KAFKA_LISTENER);
		print(contentService.getPremiumCount_1_ByContentIds(getTestContentIds(ids)), index,
				Listener.PREMIUM_COUNT_KAFKA_LISTENER);
	}

	@RequestMapping("testTotalCountByIds")
	public void testTotalCountByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getTotalCount_3_4_ByContentIds(getTestContentIds(ids)), false,
				Listener.TOTAL_COUNT_KAFKA_LISTENER);
		print(contentService.getTotalCount_34_ByContentIds(getTestContentIds(ids)), false,
				Listener.TOTAL_COUNT_KAFKA_LISTENER);
		print(contentService.getTotalCount_1_ByContentIds(getTestContentIds(ids)), index,
				Listener.TOTAL_COUNT_KAFKA_LISTENER);
	}

	@RequestMapping("testContentAllByIds")
	public void testContentAllByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getContentAllByContentIds(getTestContentIds(ids)), index,
				Listener.CONTENT_ALL_KAFKA_LISTENER);
	}

	@RequestMapping("testProductMoviesByIds")
	public void testProductMoviesByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(contentService.getProductMoviesByContentIds(getTestContentIds(ids)), index,
				Listener.PRODUCT_MOVIES_KAFKA_LISTENER);
	}

	@RequestMapping("testAllPlaylist")
	public void testAllPlaylist(@RequestParam(required = false, value = "limit") int limit) throws Exception {
		String arr[] = new String[] { Listener.PLAYLIST_IMAGE_KAFKA_LISTENER, Listener.PLAYLIST_ALLOW_KAFKA_LISTENER,
				Listener.PLAYLIST_DETAIL_KAFKA_LISTENER };
		print(playlistService.getLimitedPlaylistIds(limit), true, arr);
	}

	@RequestMapping("testPlaylistImagesByIds")
	public void testPlaylistImagesByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(playlistService.getPlaylistImagesByPlaylistIds(getTestPlaylistIds(ids)), index,
				Listener.PLAYLIST_IMAGE_KAFKA_LISTENER);
	}

	@RequestMapping("testPlaylistAllowsByIds")
	public void testPlaylistAllowsByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(playlistService.getPlaylistAllowsByPlaylistIds(getTestPlaylistIds(ids)), index,
				Listener.PLAYLIST_ALLOW_KAFKA_LISTENER);
	}

	@RequestMapping("testPlaylistDetailsByIds")
	public void testPlaylistDetailsByIds(@RequestParam(required = false, value = "ids") String ids,
			@RequestParam(required = false, value = "index") boolean index) throws Exception {
		print(playlistService.getPlaylistDetailsByPlaylistIds(getTestPlaylistIds(ids)), index,
				Listener.PLAYLIST_DETAIL_KAFKA_LISTENER);
	}

	@RequestMapping("testMethod")
	@Async
	public String testMethod() {
		System.out.println("Calling start");
		asyncMethod();
		System.out.println("Calling end");
		return "success";
	}

	@Async
	private void asyncMethod() {
		for (int i = 0; i < 5; i++) {
			System.out.println("Count->" + i);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

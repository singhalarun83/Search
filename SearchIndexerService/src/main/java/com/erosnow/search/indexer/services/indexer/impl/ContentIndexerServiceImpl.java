package com.erosnow.search.indexer.services.indexer.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.util.SolrFields;
import com.erosnow.search.indexer.services.dataImport.service.ContentService;
import com.erosnow.search.indexer.services.dto.SolrDocumentBuilder;
import com.erosnow.search.indexer.services.indexer.ContentIndexerService;
import com.erosnow.search.indexer.services.indexer.SolrIndexerService;
import com.erosnow.search.indexer.services.util.SolrDocumentUtil;

@Service("contentIndexerService")
public class ContentIndexerServiceImpl implements ContentIndexerService {

	@Autowired
	private ContentService contentService;

	@Autowired
	private SolrIndexerService solrIndexerService;

	public void indexRoles(List<String> contentIds) {
		List<Map<String, Object>> roles = contentService.getRolesByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> role : roles) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildRolesDocument(role);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexAllowedOrBlocked(List<String> contentIds) {
		List<Map<String, Object>> allowBlocks = contentService.getAllowBlockByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> allowBlock : allowBlocks) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildAllowBlocksDocument(allowBlock);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexContentDetails(List<String> contentIds) {
		List<Map<String, Object>> contentDetails = contentService.getContentDetailByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> contentDetail : contentDetails) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildContentDetailsDocument(contentDetail);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexGenres(List<String> contentIds) {
		List<Map<String, Object>> genres = contentService.getGenresByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> genre : genres) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildGenresDocument(genre);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexKeywords(List<String> contentIds) {
		List<Map<String, Object>> keywords = contentService.getKeywordsByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> keyword : keywords) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildKeywordsDocument(keyword);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexParentContents(List<String> contentIds) {
		List<Map<String, Object>> parentContents = contentService.getParentContentByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> parentContent : parentContents) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildParentContentsDocument(parentContent);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexContentAssetProducts(List<String> contentIds) {
		List<Map<String, Object>> products = contentService.getContentAssetProductsByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> product : products) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildContentAssetProductsDocument(product);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexContentProducts(List<String> contentIds) {
		List<Map<String, Object>> products = contentService.getContentProductsByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> product : products) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildContentProductsDocument(product);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexSubtitles(List<String> contentIds) {
		List<Map<String, Object>> subtitles = contentService.getSubtitlesByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> subtitle : subtitles) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildSubtitlesDocument(subtitle);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexRecommendations(List<String> contentIds) {
		List<Map<String, Object>> recommendations = contentService.getRecommendationsByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> recommendation : recommendations) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildRecommendations(recommendation);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexContentAssetImagePaths(List<String> contentIds) {
		List<Map<String, Object>> imagePaths = contentService.getContentAssetImagePathsByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> imagePath : imagePaths) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildContentAssetImagePathsDocument(imagePath);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexContentImagePaths(List<String> contentIds) {
		List<Map<String, Object>> imagePaths = contentService.getContentImagePathsByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> imagePath : imagePaths) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildContentImagePathsDocument(imagePath);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexContentAll(List<String> contentIds) {
		List<Map<String, Object>> contentAll = contentService.getContentAllByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> content : contentAll) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildContentAllDocument(content);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexBasicCounts(List<String> contentIds) {
		List<Map<String, Object>> basicCounts = contentService.getBasicCountByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> basicCount : basicCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildBasicCountsDocument(basicCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	@SuppressWarnings("serial")
	public void indexPreminumCounts(List<String> contentIds) {
		List<Map<String, Object>> premiumCounts_3_4 = contentService.getPremiumCount_3_4_ByContentIds(contentIds);
		List<Map<String, Object>> premiumCounts_34 = contentService.getPremiumCount_34_ByContentIds(contentIds);
		List<Map<String, Object>> premiumCounts_1 = contentService.getPremiumCount_1_ByContentIds(contentIds);
		List<Map<String, Object>> premiumCounts = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> premiumCount : premiumCounts_3_4) {
			if (premiumCount.get(SolrFields.PREMIUM_GEO_ALLOWED) != null
					|| premiumCount.get(SolrFields.PREMIUM_COUNT) != null) {
				premiumCounts.add(premiumCount);
				contentIds.remove(String.valueOf(premiumCount.get(SolrFields.ID)));
			}
		}
		for (Map<String, Object> premiumCount : premiumCounts_34) {
			if (premiumCount.get(SolrFields.PREMIUM_GEO_ALLOWED) != null
					|| premiumCount.get(SolrFields.PREMIUM_COUNT) != null) {
				premiumCounts.add(premiumCount);
				contentIds.remove(String.valueOf(premiumCount.get(SolrFields.ID)));
			}
		}
		for (Map<String, Object> premiumCount : premiumCounts_1) {
			if (premiumCount.get(SolrFields.PREMIUM_GEO_ALLOWED) != null
					|| premiumCount.get(SolrFields.PREMIUM_COUNT) != null) {
				premiumCounts.add(premiumCount);
				contentIds.remove(String.valueOf(premiumCount.get(SolrFields.ID)));
			}
		}
		for (final String contentId : contentIds) {
			premiumCounts.add(new HashMap<String, Object>() {
				{
					put(SolrFields.ID, contentId);
					put(SolrFields.PREMIUM_GEO_ALLOWED, null);
					put(SolrFields.PREMIUM_COUNT, null);
				}
			});
		}
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> premiumCount : premiumCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildPremiumCountsDocument(premiumCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexPreminumCounts_3_4(List<String> contentIds) {
		List<Map<String, Object>> premiumCounts = contentService.getPremiumCount_3_4_ByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> premiumCount : premiumCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildPremiumCountsDocument(premiumCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexPreminumCounts_34(List<String> contentIds) {
		List<Map<String, Object>> premiumCounts = contentService.getPremiumCount_34_ByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> premiumCount : premiumCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildPremiumCountsDocument(premiumCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexPreminumCounts_1(List<String> contentIds) {
		List<Map<String, Object>> premiumCounts = contentService.getPremiumCount_1_ByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> premiumCount : premiumCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildPremiumCountsDocument(premiumCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	@SuppressWarnings("serial")
	public void indexTotalCounts(List<String> contentIds) {
		List<Map<String, Object>> totalCounts_3_4 = contentService.getTotalCount_3_4_ByContentIds(contentIds);
		List<Map<String, Object>> totalCounts_34 = contentService.getTotalCount_34_ByContentIds(contentIds);
		List<Map<String, Object>> totalCounts_1 = contentService.getTotalCount_1_ByContentIds(contentIds);
		List<Map<String, Object>> totalCounts = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> totalCount : totalCounts_3_4) {
			if (totalCount.get(SolrFields.ASSET_CONTENT_ALLOWED) != null
					|| totalCount.get(SolrFields.TOTAL_CONTENTS) != null) {
				totalCount.put(SolrFields.START_DATE, null);
				totalCount.put(SolrFields.END_DATE, null);
				totalCounts.add(totalCount);
				contentIds.remove(String.valueOf(totalCount.get(SolrFields.ID)));
			}
		}
		for (Map<String, Object> totalCount : totalCounts_34) {
			if (totalCount.get(SolrFields.ASSET_CONTENT_ALLOWED) != null
					|| totalCount.get(SolrFields.TOTAL_CONTENTS) != null
					|| totalCount.get(SolrFields.START_DATE) != null || totalCount.get(SolrFields.END_DATE) != null) {
				totalCounts.add(totalCount);
				contentIds.remove(String.valueOf(totalCount.get(SolrFields.ID)));
			}
		}
		for (Map<String, Object> totalCount : totalCounts_1) {
			if (totalCount.get(SolrFields.ASSET_CONTENT_ALLOWED) != null
					|| totalCount.get(SolrFields.TOTAL_CONTENTS) != null) {
				totalCount.put(SolrFields.START_DATE, null);
				totalCount.put(SolrFields.END_DATE, null);
				totalCounts.add(totalCount);
				contentIds.remove(String.valueOf(totalCount.get(SolrFields.ID)));
			}
		}
		for (final String contentId : contentIds) {
			totalCounts.add(new HashMap<String, Object>() {
				{
					put(SolrFields.ID, contentId);
					put(SolrFields.ASSET_CONTENT_ALLOWED, null);
					put(SolrFields.TOTAL_CONTENTS, null);
					put(SolrFields.START_DATE, null);
					put(SolrFields.END_DATE, null);
				}
			});
		}
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> totalCount : totalCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildPremiumCountsDocument(totalCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexTotalCounts_3_4(List<String> contentIds) {
		List<Map<String, Object>> totalCounts = contentService.getTotalCount_3_4_ByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> totalCount : totalCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildTotalCountsDocument(totalCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexTotalCounts_34(List<String> contentIds) {
		List<Map<String, Object>> totalCounts = contentService.getTotalCount_34_ByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> totalCount : totalCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildTotalCountsDocument(totalCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexTotalCounts_1(List<String> contentIds) {
		List<Map<String, Object>> totalCounts = contentService.getTotalCount_1_ByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> totalCount : totalCounts) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildTotalCountsDocument(totalCount);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void indexProductMovies(List<String> contentIds) {
		List<Map<String, Object>> productMovies = contentService.getProductMoviesByContentIds(contentIds);
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (Map<String, Object> productMovie : productMovies) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildProductMoviesDocument(productMovie);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}

	public void deleteById(List<String> uniqueIds) {
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (String uniqueId : uniqueIds) {
			SolrDocumentBuilder sdb = SolrDocumentUtil.buildToBeDeletedDocument(uniqueId);
			docs.add(sdb.build());
		}
		solrIndexerService.index(docs);
	}
}

package com.erosnow.search.searcher.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erosnow.search.base.model.common.ServiceResponse;
import com.erosnow.search.base.model.ext.SearchRequest;
import com.erosnow.search.base.model.ext.SearchResponse;
import com.erosnow.search.base.model.ext.SearchSRO;
import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.util.RequestClientType;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.searcher.services.SearchSearcherServiceFactory;
import com.erosnow.search.searcher.services.util.QueryBuilder;
import com.erosnow.search.searcher.services.util.RequestParser;
import com.google.common.util.concurrent.RateLimiter;

@RestController
@RequestMapping("/service/searchSearcher/")
public class SearcherQueryController {

	private static final Logger LOG = LoggerFactory.getLogger(SearcherQueryController.class);

	@Autowired
	private SearchSearcherServiceFactory searchSearcherServiceFactory;

	@RequestMapping(value = "textSearch")
	public SearchResponse search(@RequestBody SearchRequest request) {
		SearchResponse response = successSearchResponse("Request completed successfully.");
		if (!validateSearchRequest(request, response) || !validateSearchRequestParams(request, response)) {
			return response;
		}
		QueryBuilder builder = RequestParser.parseRequest(request);
		try {
			SearchSRO searchSRO = searchSearcherServiceFactory.getSearchService().search(builder);
			response.setSearchSRO(searchSRO);
		} catch (Exception e) {
			LOG.error("Exception while searching->" + e.getMessage(), e);
			response.setCode(ServiceResponse.ResponseStatus.FAIL.responseType());
			response.setMessage("Request failed!" + e.getMessage());
		}
		return response;
	}

	private boolean validateSearchRequestParams(SearchRequest request, SearchResponse response) {
		int number = request.getNumber();
		if (number <= 0) {
			LOG.error("Rejecting request as the number is ", number);
			response.setMessage("Invalid max search result in the request ");
			response.setCode(ServiceResponse.ResponseStatus.FAIL.responseType());
			return false;
		}
		SearchPropertyCache cache = CacheManager.getInstance().getCache(SearchPropertyCache.class);
		int maxStart = cache.getIntegerProperty(SearchPropertyEnum.SEARCH_MAX_START);
		int start = request.getStart();
		if (start > maxStart) {
			LOG.error("Start was found to be {} crossing the limit {}", start, maxStart);
			response.setMessage("Invalid max start in the request ");
			response.setCode(ServiceResponse.ResponseStatus.FAIL.responseType());
		}
		return true;
	}

	private boolean validateSearchRequest(SearchRequest request, SearchResponse response) {
		RequestClientType requestClient = RequestClientType.get(request.getRequestClient());
		RateLimiter rateLimiter = requestClient.getRateLimiter();
		if (rateLimiter.tryAcquire()) {
			return true;
		} else {
			LOG.error("Maximum allowed requests reached for client {} ", requestClient.toString());
			response.setMessage("Too many request, Maximum allowed is : " + requestClient.getLimit());
			response.setCode(ServiceResponse.ResponseStatus.FAIL.responseType());
			return false;
		}
	}

	private SearchResponse successSearchResponse(String msg) {
		SearchResponse response = new SearchResponse(ServiceResponse.ResponseStatus.SUCCESS.responseType(), msg);
		return response;
	}

	private SearchResponse failureSearchResponse(String msg) {
		SearchResponse response = new SearchResponse(ServiceResponse.ResponseStatus.FAIL.responseType(), msg);
		return response;
	}

}

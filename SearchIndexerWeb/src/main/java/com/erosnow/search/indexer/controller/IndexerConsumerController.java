package com.erosnow.search.indexer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erosnow.search.base.model.common.ServiceResponse;
import com.erosnow.search.common.cache.CacheManager;
import com.erosnow.search.common.cache.impl.IndexerConfigurationCache;
import com.erosnow.search.common.cache.impl.SearchPropertyCache;
import com.erosnow.search.common.entity.ConsumerTypeConfiguration;
import com.erosnow.search.common.util.SearchPropertyEnum;
import com.erosnow.search.indexer.services.SearchIndexerServiceFactory;
import com.erosnow.search.indexer.services.consumer.ConsumerSpawnService;
import com.erosnow.search.indexer.services.dto.KafkaPushDTO;
import com.erosnow.search.indexer.services.producer.RequeueService;

@RestController
@RequestMapping("/service/searchIndexer/")
public class IndexerConsumerController {

	private static final String AUTH_FAILURE = "Authentication FAILURE !!!";

	@Autowired
	SearchIndexerServiceFactory searchIndexerServiceFactory;

	@Autowired
	RequeueService requeueService;

	private static final Logger LOG = LoggerFactory.getLogger(IndexerConsumerController.class);

	@RequestMapping("registerConsumer")
	public ServiceResponse register(@RequestParam("consumerType") String consumerType,
			@RequestParam(value = "pwd") String password, @RequestParam("consumerCount") int consumerCount,
			@RequestParam("groupId") String groupId) {
		if (!validatePassword(password))
			return failureResponse(AUTH_FAILURE);
		try {
			spawnInstances(consumerType, consumerCount, groupId);
			return successResponse(
					"Registered {" + consumerCount + "} instances of {" + consumerType + "} successfully...");
		} catch (Exception e) {
			LOG.error("Exception while registering consumer: ", e);
			return failureResponse("Exception while registering consumer: " + e.getMessage());
		}
	}

	private ServiceResponse successResponse(String msg) {
		ServiceResponse response = new ServiceResponse(ServiceResponse.ResponseStatus.SUCCESS.responseType());
		response.setMessage(msg);
		return response;
	}

	private ServiceResponse failureResponse(String msg) {
		ServiceResponse response = new ServiceResponse(ServiceResponse.ResponseStatus.FAIL.responseType());
		response.setMessage(msg);
		return response;
	}

	private boolean validatePassword(String password) {
		if (CacheManager.getInstance().getCache(SearchPropertyCache.class)
				.getProperty(SearchPropertyEnum.RELOAD_CACHE_PASSWORD).equals(password)) {
			return true;
		} else {
			return false;
		}
	}

	private String spawnInstances(String consumerType, int number, String groupId) throws Exception {
		ConsumerTypeConfiguration config = CacheManager.getInstance().getCache(IndexerConfigurationCache.class)
				.getConsumerTypeConfigurationByType(consumerType);
		StringBuilder sb = new StringBuilder();
		ConsumerSpawnService spawnService = searchIndexerServiceFactory.getConsumerSpawnService(consumerType);
		if (config.isEnabled()) {
			spawnService.spawnListenersByType(consumerType, number, groupId);
			sb.append("Registered successfully to the queue... ").append(config.getQueueName());
		} else {
			LOG.error("Can not register disabled consumer {} ", config.getQueueName());
			sb.append("Can not register disabled consumer {} " + config.getQueueName());
		}
		return sb.toString();
	}

	@RequestMapping(value = "flushAll")
	public ServiceResponse flushAll(@RequestParam(value = "pwd") String password) {
		if (!validatePassword(password))
			return failureResponse(AUTH_FAILURE);
		try {
			List<ConsumerSpawnService> services = searchIndexerServiceFactory.getAllFlushableServices();
			for (ConsumerSpawnService service : services) {
				service.flushAll();
			}
			return successResponse("Flushed accumulated objects successfully...");
		} catch (Exception e) {
			LOG.error("Exception while flushing: ", e);
			return failureResponse("Error while flushing: " + e.getMessage());
		}
	}

	@RequestMapping(value = "unregister")
	public ServiceResponse unregister(@RequestParam(value = "pwd") String password) {
		if (!validatePassword(password))
			return failureResponse(AUTH_FAILURE);
		try {
			for (ConsumerTypeConfiguration consumerType : CacheManager.getInstance()
					.getCache(IndexerConfigurationCache.class).getAllConsumerTypes()) {
				ConsumerSpawnService spawnService = searchIndexerServiceFactory
						.getConsumerSpawnService(consumerType.getName());
				spawnService.unregisterInstances(consumerType.getName());
			}
			return successResponse("Unregistered all the consumers successfully...");
		} catch (Exception e) {
			LOG.error("error while unregistering consumers: ", e);
			return failureResponse("error while unregistering consumers: " + e.getMessage());
		}
	}

	@RequestMapping(value = "unregisterByType")
	public ServiceResponse unregisterByType(@RequestParam(value = "pwd") String password,
			@RequestParam("consumerType") String consumerType, @RequestParam("groupId") String groupId) {
		if (!validatePassword(password))
			return failureResponse(AUTH_FAILURE);
		ConsumerSpawnService spawnService = searchIndexerServiceFactory.getConsumerSpawnService(consumerType);
		try {
			spawnService.unregisterInstances(consumerType, groupId);
			return successResponse("Unregistered all the consumers successfully... for type: " + consumerType);
		} catch (Exception ex) {
			LOG.error("error while unregistering consumers ", ex);
			return failureResponse(
					"error while unregistering consumers... for type: " + consumerType + " " + ex.getMessage());
		}
	}

	@RequestMapping(value = "showConsumerStatus")
	public ServiceResponse showConsumerStatus(@RequestParam(value = "pwd") String password) {
		if (!validatePassword(password))
			return failureResponse(AUTH_FAILURE);
		Map<String, Integer> consumerTypeToCountMap = new HashMap<String, Integer>();
		try {
			List<ConsumerSpawnService> services = searchIndexerServiceFactory.getAllConsumerSpawnServices();
			for (ConsumerSpawnService service : services) {
				consumerTypeToCountMap.putAll(service.getCompleteConsumerStatus());
			}
			if (consumerTypeToCountMap.isEmpty()) {
				return successResponse("No registered consumers!");
			}
			ServiceResponse response = successResponse("Retreived consumer status successfully...");
			response.setObject(consumerTypeToCountMap);
			return response;
		} catch (Exception ex) {
			LOG.error("error while retreiving consumer status ", ex);
			return failureResponse("error while retreiving consumer status: " + ex.getMessage());
		}
	}

	@RequestMapping(value = "showConsumerStatusByType")
	public ServiceResponse showConsumerStatusByType(@RequestParam(value = "pwd") String password,
			@RequestParam("consumerType") String consumerType) {
		if (!validatePassword(password))
			return failureResponse(AUTH_FAILURE);
		try {
			ConsumerSpawnService spawnService = searchIndexerServiceFactory.getConsumerSpawnService(consumerType);
			return successResponse(spawnService.getConsumerStatusByType(consumerType));
		} catch (Exception ex) {
			LOG.error("error while retreiving consumer status ", ex);
			return failureResponse("error while retreiving consumer status: " + ex.getMessage());
		}
	}

	@RequestMapping(value = "pushSample")
	public ServiceResponse pushSample(@RequestParam(value = "pwd") String password,
			@RequestParam("consumerType") String consumerType) {
		if (!validatePassword(password))
			return failureResponse(AUTH_FAILURE);
		try {
			KafkaPushDTO dto = new KafkaPushDTO("SampleObject", 1l);
			requeueService.requeue(consumerType, dto);
			return successResponse("Object pushed successfully...");
		} catch (Exception ex) {
			LOG.error("error while pushing object ", ex);
			return failureResponse("error while pushing object: " + ex.getMessage());
		}
	}
}

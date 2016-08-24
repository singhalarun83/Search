package com.erosnow.search.indexer.services.producer;

import java.io.Serializable;

import com.erosnow.search.common.util.Listener;

public interface RequeueService {

	public static final String CONTENT_LISTENERS[] = new String[] { Listener.CONTENT_KAFKA_LISTENER,
			Listener.ROLE_KAFKA_LISTENER, Listener.KEYWORD_KAFKA_LISTENER, Listener.RECOMMENDATION_KAFKA_LISTENER,
			Listener.CONTENT_IMAGEPATH_KAFKA_LISTENER, Listener.CONTENT_ASSET_IMAGEPATH_KAFKA_LISTENER,
			Listener.CONTENT_ALL_KAFKA_LISTENER, Listener.CONTENT_ASSET_PRODUCT_KAFKA_LISTENER,
			Listener.CONTENT_PRODUCT_KAFKA_LISTENER, Listener.PRODUCT_MOVIES_KAFKA_LISTENER,
			Listener.BASIC_COUNT_KAFKA_LISTENER, Listener.PREMIUM_COUNT_KAFKA_LISTENER,
			Listener.TOTAL_COUNT_KAFKA_LISTENER, Listener.ALLOW_BLOCK_KAFKA_LISTENER, Listener.GENRE_KAFKA_LISTENER,
			Listener.PARENT_CONTENT_KAFKA_LISTENER, Listener.SUBTITLE_KAFKA_LISTENER };
	public static final String PLAYLIST_LISTENERS[] = new String[] { Listener.PLAYLIST_IMAGE_KAFKA_LISTENER,
			Listener.PLAYLIST_ALLOW_KAFKA_LISTENER, Listener.PLAYLIST_DETAIL_KAFKA_LISTENER };

	void requeue(String consumerType, Serializable ids) throws Exception;
}

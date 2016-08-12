package com.erosnow.search.indexer.services.listener.playlist.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.log.IndexerLogger;
import com.erosnow.search.common.util.Listener;
import com.erosnow.search.indexer.services.dto.KafkaPushDTO;
import com.erosnow.search.indexer.services.dto.KafkaPushListDTO;
import com.erosnow.search.indexer.services.listener.impl.AbstractKafkaListenerServiceImpl;

@Service(Listener.PLAYLIST_IMAGE_KAFKA_LISTENER)
@Scope("prototype")
public class PlaylistImageKafkaListenerServiceImpl extends AbstractKafkaListenerServiceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(PlaylistImageKafkaListenerServiceImpl.class);

	protected Set<String> ids = new HashSet<String>();

	public PlaylistImageKafkaListenerServiceImpl() {
		super(Listener.PLAYLIST_IMAGE_KAFKA_LISTENER);
	}

	protected synchronized void accumulate(String id) {
		ids.add(id);
	}

	protected synchronized List<String> clear() {
		List<String> idList = new ArrayList<String>(ids);
		ids.clear();
		return idList;
	}

	@Override
	public synchronized Integer getAccumulatedSetSize() {
		return ids.size();
	}

	@Override
	public synchronized void index() {
		List<String> playlistIds = null;
		try {
			playlistIds = clear();
			IndexerLogger.logExit(playlistIds, this.getClass());
			getPlaylistIndexerService().indexPlaylistImages(playlistIds);
		} catch (Exception e) {
			IndexerLogger.logError(playlistIds, this.getClass(), e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void process(byte[] message) throws Exception {
		KafkaPushListDTO messages = (KafkaPushListDTO) fstSerializationService.deserialize(message,
				KafkaPushListDTO.class);
		if (messages != null) {
			List<KafkaPushDTO> objects = (List<KafkaPushDTO>) messages.getDtos();
			logSizeMetric(objects.size());
			for (KafkaPushDTO object : objects) {
				String playlistId = (String) object.getPayload();
				logMessageHit(playlistId);
				accumulate(playlistId);
				IndexerLogger.logEnter(playlistId, this.getClass());
				if (config.getIndexSize() <= (getAccumulatedSetSize())) {
					callIndex();
				}
			}
		}

	}
}

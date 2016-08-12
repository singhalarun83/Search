package com.erosnow.search.indexer.services.listener.content.impl;

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

@Service(Listener.CONTENT_KAFKA_LISTENER)
@Scope("prototype")
public class ContentKafkaListenerServiceImpl extends AbstractKafkaListenerServiceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(ContentKafkaListenerServiceImpl.class);

	protected Set<String> ids = new HashSet<String>();

	public ContentKafkaListenerServiceImpl() {
		super(Listener.CONTENT_KAFKA_LISTENER);
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
		List<String> contentIds = null;
		try {
			contentIds = clear();
			IndexerLogger.logExit(contentIds, this.getClass());
			getContentIndexerService().indexContentDetails(contentIds);
		} catch (Exception e) {
			IndexerLogger.logError(contentIds, this.getClass(), e.getMessage());
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
				String contentId = (String) object.getPayload();
				logMessageHit(contentId);
				accumulate(contentId);
				IndexerLogger.logEnter(contentId, this.getClass());
				if (config.getIndexSize() <= (getAccumulatedSetSize())) {
					callIndex();
				}
			}
		}

	}
}

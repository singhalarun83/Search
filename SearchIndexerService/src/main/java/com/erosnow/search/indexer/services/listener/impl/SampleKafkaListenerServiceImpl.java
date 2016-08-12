package com.erosnow.search.indexer.services.listener.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.erosnow.search.common.log.DebugLogger;
import com.erosnow.search.common.util.Listener;

@Service(Listener.SAMPLE_KAFKA_LISTENER)
@Scope("prototype")
public class SampleKafkaListenerServiceImpl extends AbstractKafkaListenerServiceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(SampleKafkaListenerServiceImpl.class);

	protected Set<Object> ids = new HashSet<Object>();

	public SampleKafkaListenerServiceImpl() {
		super(Listener.SAMPLE_KAFKA_LISTENER);
	}

	protected synchronized void accumulate(Object id) {
		ids.add(id);
	}

	protected synchronized List<Object> clear() {
		List<Object> idList = new ArrayList<Object>(ids);
		ids.clear();
		return idList;
	}

	@Override
	public synchronized Integer getAccumulatedSetSize() {
		return ids.size();
	}

	@Override
	public synchronized void index() {
		List<Object> idList = clear();
		DebugLogger.log("Going to index Objects  " + idList);
		// index service call here
	}

	@SuppressWarnings({ "unchecked", "serial" })
	@Override
	protected void process(byte[] message) throws Exception {
		Object messages = (Object) fstSerializationService.deserialize(message, Object.class);
		if (messages != null) {
			List<Object> objects = null;
			if (messages instanceof List) {
				objects = ((List<Object>) messages);
			} else {
				final Object obj = messages;
				objects = new ArrayList<Object>() {
					{
						add(obj);
					}
				};
			}
			logSizeMetric(objects.size());
			for (Object object : objects) {
				logMessageHit(object);
				accumulate(object);
				if (config.getIndexSize() <= (getAccumulatedSetSize())) {
					callIndex();
				}
			}
		}

	}
}

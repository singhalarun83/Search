package com.erosnow.search.indexer.services.listener.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("sampleAMQCacheEvictListener")
@Scope("prototype")
public class SampleCacheEvictionListener<Object, V> extends AbstractCacheEvictionListener<Object, V> {

	private static final Logger LOG = LoggerFactory.getLogger(SampleCacheEvictionListener.class);

	protected synchronized void index() {
		List<Object> objects = this.clear();
		LOG.info("Going to index Onjects in evict " + objects);
	}
}

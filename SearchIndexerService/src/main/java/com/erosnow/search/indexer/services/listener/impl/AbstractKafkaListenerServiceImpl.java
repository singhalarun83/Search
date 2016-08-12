package com.erosnow.search.indexer.services.listener.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.erosnow.search.indexer.services.SearchIndexerServiceFactory;
import com.erosnow.search.indexer.services.indexer.ContentIndexerService;
import com.erosnow.search.indexer.services.indexer.PlaylistIndexerService;
import com.erosnow.search.indexer.services.indexer.SolrIndexerService;
import com.erosnow.search.indexer.services.io.FSTSerializationService;
import com.erosnow.search.indexer.services.listener.KafkaListenerService;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

public abstract class AbstractKafkaListenerServiceImpl extends AbstractListenerServiceImpl
		implements KafkaListenerService {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractKafkaListenerServiceImpl.class);

	protected KafkaStream<byte[], byte[]> stream;

	@Autowired
	protected SearchIndexerServiceFactory searchIndexerServiceFactory;

	@Autowired
	protected FSTSerializationService fstSerializationService;

	@Autowired
	private SolrIndexerService solrIndexerService;

	@Autowired
	private ContentIndexerService contentIndexerService;

	@Autowired
	private PlaylistIndexerService playlistIndexerService;

	protected String groupId;

	public AbstractKafkaListenerServiceImpl(String consumerType) {
		super(consumerType);
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setStream(KafkaStream<byte[], byte[]> stream) {
		this.stream = stream;
	}

	protected abstract void process(byte[] message) throws Exception;

	@Override
	public void run() {
		ConsumerIterator<byte[], byte[]> iterate = this.stream.iterator();
		try {
			while (iterate.hasNext()) {
				try {
					MessageAndMetadata<byte[], byte[]> nextPacket = iterate.next();
					byte[] message = nextPacket.message();
					process(message);
				} catch (Exception ex) {
					LOG.error("Error while processing message from kafka", ex);
				}
			}
		} catch (Exception ex) {
			LOG.error("Exception on message: ", ex);
			LOG.info("Clearing stream for type : " + config.getName());
			if (this.stream != null) {
				this.stream.clear();
			}
			try {
				searchIndexerServiceFactory.getConsumerSpawnService(this.consumerType)
						.unregisterInstances(this.consumerType, this.groupId);
			} catch (Exception e) {
				LOG.error("Exception while unregistering kafka stream: ", e);
			}
			LOG.info("Shutting down Thread : " + config.getName() + " instance number : " + instanceNumber);
		}
	}

	public SolrIndexerService getSolrIndexerService() {
		return solrIndexerService;
	}

	public void setSolrIndexerService(SolrIndexerService solrIndexerService) {
		this.solrIndexerService = solrIndexerService;
	}

	public ContentIndexerService getContentIndexerService() {
		return contentIndexerService;
	}

	public void setContentIndexerService(ContentIndexerService contentIndexerService) {
		this.contentIndexerService = contentIndexerService;
	}

	public PlaylistIndexerService getPlaylistIndexerService() {
		return playlistIndexerService;
	}

	public void setPlaylistIndexerService(PlaylistIndexerService playlistIndexerService) {
		this.playlistIndexerService = playlistIndexerService;
	}
}

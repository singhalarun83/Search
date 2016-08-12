package com.erosnow.search.indexer.services.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erosnow.search.indexer.services.io.FSTSerializationService;
import com.erosnow.search.indexer.services.io.FSTSerializationServiceImpl;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

public class FSTEncoder<KafkaPushListDTO> implements Encoder<com.erosnow.search.indexer.services.dto.KafkaPushListDTO> {

	private FSTSerializationService fstSerializationService = new FSTSerializationServiceImpl();

	private static final Logger LOG = LoggerFactory.getLogger(FSTEncoder.class);

	public FSTEncoder() {
	}

	public FSTEncoder(VerifiableProperties props) {
	}

	@Override
	public byte[] toBytes(com.erosnow.search.indexer.services.dto.KafkaPushListDTO arg0) {
		try {
			return fstSerializationService.doSerialize(arg0);
		} catch (Exception e) {
			LOG.error("Exception while serializing : ", e);
			throw new RuntimeException(e);
		}
	}
}

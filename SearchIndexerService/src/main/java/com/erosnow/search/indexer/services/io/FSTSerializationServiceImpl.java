package com.erosnow.search.indexer.services.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("fstSerializationService")
public class FSTSerializationServiceImpl implements FSTSerializationService {
	private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

	private static final Logger LOG = LoggerFactory.getLogger(FSTSerializationServiceImpl.class);

	static {
		conf.setForceSerializable(true);
	}

	@Override
	public byte[] doSerialize(final Object obj) throws Exception {

		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final FSTObjectOutput out = conf.getObjectOutput(byteArrayOutputStream);
		byte[] responseData = null;
		try {
			out.writeObject(obj, Object.class);
			out.flush();
		} catch (final IOException e) {
			LOG.error("IO Exception occured during FST Serialization ", e);
			throw new Exception("IO exception while creating object of request/response class.", e);
		}
		responseData = byteArrayOutputStream.toByteArray();
		try {
			byteArrayOutputStream.close();
		} catch (final IOException e) {
			LOG.error("IO Exception occured when closing byteArrayOutputStream in FST Serialization ", e);
			throw new Exception("IO exception while creating object of request/response class.", e);
		}
		return responseData;
	}

	@Override
	public Object doDeserialize(final byte[] data) throws Exception {

		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		final FSTObjectInput in = conf.getObjectInput(byteArrayInputStream);
		Object result = null;
		try {
			result = in.readObject(Object.class);
		} catch (final Exception e) {
			LOG.error("Exception occured during FST Deserialization ", e);
			throw new Exception("Unable to deserialize through fst", e);
		}
		try {
			byteArrayInputStream.close();
		} catch (final IOException e) {
			LOG.error("IO Exception occured while closing byteArrayInputStream in FST Deserialization ", e);
			throw new Exception("IO exception while creating object of request/response class.", e);
		}
		return result;
	}

	@Override
	public Object doDeserialize(final InputStream inputStream) throws Exception {

		final FSTObjectInput in = conf.getObjectInput(inputStream);
		Object result = null;
		try {
			result = in.readObject(Object.class);
		} catch (final Exception e) {
			LOG.error("Exception occured during FST Deserialization ", e);
			throw new Exception("Unable to deserialize through fst", e);
		}
		try {
			inputStream.close();
		} catch (final IOException e) {
			LOG.error("IO Exception occured while closing byteArrayInputStream in FST Deserialization ", e);
			throw new Exception("IO exception while creating object of request/response class.", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
		return (T) doDeserialize(bytes);
	}

	public <T> byte[] serialize(T serializableObj, Class<T> objectClass) throws Exception {
		return doSerialize(objectClass);
	}
}

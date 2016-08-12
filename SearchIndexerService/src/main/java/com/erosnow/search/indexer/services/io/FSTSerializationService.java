package com.erosnow.search.indexer.services.io;

import java.io.InputStream;

public interface FSTSerializationService {

	public byte[] doSerialize(final Object obj) throws Exception;

	public Object doDeserialize(final byte[] data) throws Exception;

	public Object doDeserialize(final InputStream inputStream) throws Exception;

	public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception;

	public <T> byte[] serialize(T serializableObj, Class<T> objectClass) throws Exception;
}
